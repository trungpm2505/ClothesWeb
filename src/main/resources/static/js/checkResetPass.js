var inputList = ['newPass','newPassAgain'];
		
		inputList.forEach(function(inputName) {
			
		  var input = document.getElementById(inputName);
		  var error = document.getElementById(inputName + '-error');
		  input.addEventListener("focus", function() {
			 error.textContent = '';
		  });
		});
	var email = localStorage.getItem('email');
		function checkResetPassword() {
			
		    var newPassword = document.getElementById("newPass").value.trim();
		    var confirmNewPassword = document.getElementById('newPassAgain').value.trim();
		    
		    var formData = new FormData();
		   
		    formData.append('newPass', newPassword);
		    formData.append('newPassAgain', confirmNewPassword);
		    formData.append('email', email);
		    
		    
		    
		
		    $.ajax({
		        url: "/password/checkResetPass",
		        type: "PUT",
		        contentType: false,
		        processData: false,
		        data: formData,
		        dataType: 'json',
		         
		        success: function(data) {
		      
		        	window.location.href = '/password/resetPassSuccess';
		        },
		        error: function(jqXHR, textStatus, errorMessage) {
					 	
		             	
			      	 var errors = jqXHR.responseJSON;
				    	
						alert("có error" + errors);
						alert("errors.hasOwnProperty"+errors.hasOwnProperty("bindingErrors"));
				        if (errors.hasOwnProperty("bindingErrors")) {
			                var bindingError = errors.bindingErrors;
			                for (var i = 0; i < bindingError.length; i++) {
			                    var error = bindingError[i];
			                    $("#" + error.field + "-error").text(error.defaultMessage);
			                }
			            }
			
			            if (errors.hasOwnProperty("message")) {
			                $("#newPassAgain-error").text(errors.message);
			            }
				    
			      	        
			      		
			      	}
		    	});
	}