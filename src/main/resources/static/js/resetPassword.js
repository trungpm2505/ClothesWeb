
			
		  var input = document.getElementById("email");
		  var error = document.getElementById('email-error');
		  input.addEventListener("focus", function() {
			 error.textContent = '';
		  });

		function resetPassword() {
			
			var email = document.getElementById('email').value.trim();
		    
		
		    $.ajax({
		        url: "/password/sentEmail",
		        type: "POST",
		        contentType: 'application/json',
		        processData: false,
		        data: JSON.stringify(email),
		        dataType: 'json',
		        success: function(data) {
		        	localStorage.setItem('email', email);
		        	window.location.href = '/password/getTokenReset';
		        	
		        },
		        error: function(jqXHR, textStatus, errorMessage) {

					 	
		             	  if (jqXHR.status === 400) {
			      	       var errors = jqXHR.responseJSON;
			      	      	$("#email-error").text(errors.message);
			      	        
			      			}
			      	}
		    	});
	}