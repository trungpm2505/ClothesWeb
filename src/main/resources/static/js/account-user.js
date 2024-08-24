$(document).ready(function() {
    // Xóa thông báo lỗi khi bắt đầu nhập vào trường input
    $('#updateAccountForm input').on('input', function() {
        var fieldId = $(this).attr('id');
        $('#' + fieldId + '-error').text('');
    });

    $('#updateAccountForm').submit(function(e) {
        e.preventDefault(); // Ngăn chặn hành vi submit form mặc định

        // Lấy dữ liệu từ form
        var formData = new FormData(this);
        
        $.ajax({
            url: $(this).attr('action'),
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                $('.text-danger.form-text').empty();
                
                // Xử lý kết quả thành công
                if (response.successMessage) {
                    $('#messageText').text(response.successMessage);
                    $('#successMessage').fadeIn();

                    setTimeout(function() {
                      $('#successMessage').fadeOut();
                    }, 2000);

                }
            },
            error: function(xhr, status, error) {
                var errors = xhr.responseJSON;
                if (errors) {
                    // Xóa các thông báo lỗi cũ
                    $('.text-danger.form-text').empty();
                    
                    $.each(errors, function(field, errorMessage) {
                        $('#' + field + '-error').text(errorMessage); 
                    });
                }
            }
        });
    });
});
