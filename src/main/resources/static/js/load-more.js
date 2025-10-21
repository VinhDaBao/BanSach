document.addEventListener("DOMContentLoaded", function() {
  const loadMoreBtn = document.getElementById("load-more");
  const container = document.getElementById("books-container");

  if (!loadMoreBtn || !container) return;

  loadMoreBtn.addEventListener("click", function() {
    const url = loadMoreBtn.dataset.url;
    let offset = parseInt(loadMoreBtn.dataset.offset || "0", 10);
    const limit = parseInt(loadMoreBtn.dataset.limit || "10", 10);

    loadMoreBtn.disabled = true;
    loadMoreBtn.textContent = "Đang tải...";

    fetch(`${url}?offset=${offset}&limit=${limit}`)
      .then(response => response.json())
      .then(data => {
        // ✅ Thêm HTML mới vào container
        container.insertAdjacentHTML("beforeend", data.html);

        // ✅ Cập nhật lại offset
        loadMoreBtn.dataset.offset = data.nextOffset;

        // ✅ Kiểm tra còn dữ liệu không
        if (data.hasMore) {
          loadMoreBtn.disabled = false;
          loadMoreBtn.textContent = "Xem thêm";
        } else {
          loadMoreBtn.style.display = "none";
        }
      })
      .catch(err => {
        console.error("Lỗi khi tải thêm sách:", err);
        loadMoreBtn.disabled = false;
        loadMoreBtn.textContent = "Xem thêm";
      });
  });
});
