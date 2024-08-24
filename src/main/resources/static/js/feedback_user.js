/**
 * 
 */
 
		$(document).ready(function() {
			// Xóa thông báo lỗi khi bắt đầu nhập vào trường input
			$('#contact-form input, #contact-form textarea').on('input', function() {
				var fieldId = $(this).attr('id');
				$('#' + fieldId + '-error').text('');
			});

			// Gán sự kiện submit cho form
			$('#contact-form').submit(function(e) {
				e.preventDefault(); // Ngăn chặn hành vi submit form mặc định

				var formData = {
					fullName : $('#fullName').val(),
					email : $('#email').val(),
					phone : $('#phone').val(),
					subjectName : $('#subjectName').val(),
					note : $('#note').val()
				};

				$.ajax({
					url : '/feedback/user/add',
					type : 'POST',
					data : JSON.stringify(formData),
					contentType : 'application/json',
					success : function(response) {
						$('.text-danger.form-text').empty();

						// Xử lý kết quả thành công
						if (response.successMessage) {
							$('#messageText').text(response.successMessage);
							$('#successMessage').fadeIn();

							setTimeout(function() {
								$('#successMessage').fadeOut();
							}, 2000);

							// Xóa giá trị trong các trường input sau khi gửi thành công
							$('#subjectName').val('');
							$('#note').val('');
						}
					},
					error : function(xhr, status, error) {
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