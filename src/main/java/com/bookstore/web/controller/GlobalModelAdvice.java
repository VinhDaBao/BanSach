package com.bookstore.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.bookstore.web.entity.TheLoai;
import com.bookstore.web.service.TheLoaiService;

@ControllerAdvice
public class GlobalModelAdvice {

    @Autowired
    private TheLoaiService theLoaiService;

    @ModelAttribute("listTheLoai")
    public List<TheLoai> addTheLoaiToModel() {
        return theLoaiService.getAllTheLoai();
    }
}