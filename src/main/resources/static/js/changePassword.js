var inputList = ['currentPassword','newPassword','confirmNewPassword'];
		
		inputList.forEach(function(inputName) {
			
		  var input = document.getElementById(inputName);
		  var error = document.getElementById(inputName + '-error');
		  input.addEventListener("focus", function() {
			 error.textContent = '';
		  });
		});
	function changePassword() {
			
			var currentPassword = document.getElementById('currentPassword').value.trim();
		    var newPassword = document.getElementById("newPassword").value.trim();
		    var confirmNewPassword = document.getElementById('confirmNewPassword').value.trim();
		    
		    var formData = new FormData();
			csrfToken = Cookies.get('XSRF-TOKEN');

		   
		    formData.append('currentPassword', currentPassword);
		    formData.append('newPassword', newPassword);
		    formData.append('confirmNewPassword', confirmNewPassword);
		    
		    
		    
		
		    $.ajax({
		        url: "/password/checkChange",
		        type: "PUT",
		        contentType: false,
		        processData: false,
		        data: formData,
		        dataType: 'json',
		         headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
		        success: function(data) {
		        	document.getElementById('currentPassword').value="";
		        	document.getElementById("newPassword").value="";
		        	document.getElementById('confirmNewPassword').value="";
		        	$('#successMessage').fadeIn();

	                	setTimeout(function() {
	                	  $('#successMessage').fadeOut();
	                	}, 2000);
		        },
		        error: function(jqXHR, textStatus, errorMessage) {
					 	
		             	  //if (jqXHR.status === 400) {
			      	       var errors = jqXHR.responseJSON;
			      	      
			      	         if (errors) {
								if (errors.hasOwnProperty("bindingErrors")) {
				      	            var bindingErrors = errors["bindingErrors"];
				      	            for (var i = 0; i < bindingErrors.length; i++) {
				      	                var error = bindingErrors[i];
									    // Hiển thị thông báo lỗi trong tag ID đã tạo
									    $("#" + error.field+"-error").text(error.defaultMessage);
				      	            }
			      	        	}
								
			      	            if (errors.hasOwnProperty("currentPassword")) {
				      	            var curentPassword = errors["currentPassword"];
									    // Hiển thị thông báo lỗi trong tag ID đã tạo
									$("#currentPassword-error").text(curentPassword);
			      	        	}
			      	        	
			      	        	   if (errors.hasOwnProperty("confirmNewPassword")) {
				      	            var curentPassword = errors["confirmNewPassword"];
									    // Hiển thị thông báo lỗi trong tag ID đã tạo
									$("#confirmNewPassword-error").text(curentPassword);
			      	        	}
			      	         }
			      	        
			      			//}}
			      	}
		    	});
	}