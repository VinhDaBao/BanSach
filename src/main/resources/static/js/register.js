function checkUsername(taiKhoan) {
    const errorDiv = document.getElementById('username-error');
    const successDiv = document.getElementById('username-success');
	    
    fetch('/check-username?taiKhoan=' + encodeURIComponent(taiKhoan))
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                errorDiv.textContent = 'Tên đăng nhập đã tồn tại!';
                errorDiv.style.display = 'block';
                successDiv.style.display = 'none';
                document.getElementById('taiKhoan').style.borderColor = 'red';
            } else {
                errorDiv.style.display = 'none';
                successDiv.style.display = 'block';
                document.getElementById('taiKhoan').style.borderColor = '#4facfe';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            errorDiv.textContent = 'Lỗi kiểm tra, thử lại!';
            errorDiv.style.display = 'block';
        });
}