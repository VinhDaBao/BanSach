package com.bookstore.web.controller;

import com.bookstore.web.entity.Sach;
import com.bookstore.web.service.SachService;
import com.bookstore.web.service.CaculatingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.WebContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;
import org.thymeleaf.web.servlet.IServletWebExchange;

@Controller
public class ProductApiController {
    private static final Logger log = LoggerFactory.getLogger(ProductApiController.class);

    private final CaculatingService cal;
    private final SachService sachService;
    private final SpringTemplateEngine templateEngine;

    // Constructor injection ensures Spring cung cấp các bean (không dùng "new")
    public ProductApiController(SachService sachService,
                                SpringTemplateEngine templateEngine,
                                CaculatingService cal) {
        this.sachService = sachService;
        this.templateEngine = templateEngine;
        this.cal = cal;
    }

    @GetMapping(value = "/products/fragment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> productsFragment(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            HttpServletRequest request,
            HttpServletResponse response) {
        JakartaServletWebApplication webApp = JakartaServletWebApplication.buildApplication(request.getServletContext());

    	    IServletWebExchange exchange = webApp.buildExchange(request, response);

        if (offset < 0) offset = 0;
        if (limit <= 0) limit = 10;

        log.info("productsFragment called: offset={}, limit={}", offset, limit);

        // convert offset (records already loaded) -> pageNo (1-based) because your service uses pageNo-1
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<Sach> page = sachService.getAllSach(pageable.getPageNumber() + 1, limit);

        // use injected calculation service (it now has its dependencies injected by Spring)
        cal.calculateDiscounts(page);
        cal.calculateRatings(page);

        long total = sachService.countAll();

        WebContext ctx = new WebContext(exchange, request.getLocale());
        // truyền biến phù hợp với fragment của bạn (thay "items" nếu fragment dùng tên khác)
        ctx.setVariable("items", page.getContent());
        ctx.setVariable("page", page);
        
        String html = templateEngine.process("fragments/book-cards", ctx);

        int returned = page.getNumberOfElements();
        boolean hasMore = offset + returned < total;
        int nextOffset = offset + returned;

        Map<String, Object> result = new HashMap<>();
        result.put("html", html);
        result.put("hasMore", hasMore);
        result.put("nextOffset", nextOffset);

        log.info("productsFragment returning: returned={}, hasMore={}, nextOffset={}", returned, hasMore, nextOffset);
        return ResponseEntity.ok(result);
    }
}