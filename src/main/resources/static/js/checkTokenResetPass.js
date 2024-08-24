		  var input = document.getElementById("token");
		  var error = document.getElementById('token-error');
		  input.addEventListener("focus", function() {
			 error.textContent = '';
		  });
		
	
	var email = localStorage.getItem('email');
	function checkToken() {
			var token = document.getElementById('token').value.trim();
		     
			var checkTokenResetPass = {
				  email:email, 
				  token: token
				};
		    $.ajax({
		        url: "/password/checkTokenReset",
		        type: "POST",
		        contentType: 'application/json',
		        data: JSON.stringify(checkTokenResetPass),
		        dataType: 'json',
		        success: function(data) {
		        	window.location.href = '/password/setNewPassView';
		        },
		        error: function(jqXHR, textStatus, errorMessage) {
					if (jqXHR.status === 400) {
			      	       var errors = jqXHR.responseJSON;
							$("#token-error").text(errors.message);	        
			      	}
					 
			      	}
		    	});
	}
	
	function resent() {
			
			input.value()='';
		    $.ajax({
		        url: "/password/sentEmail",
		        type: "POST",
		        contentType: 'application/json',
		        processData: false,
		        data: JSON.stringify(email),
		        dataType: 'json',
		        success: function(data) {
		        	
		        },
		        error: function(jqXHR, textStatus, errorMessage) {
						alert("The system has problem!")
			      	}
		    	});
	}