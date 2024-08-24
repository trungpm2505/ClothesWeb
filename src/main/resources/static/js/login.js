	const loginForm = document.getElementById('login-form');
		const emailInput = document.getElementById('email-input');
		const passwordInput = document.getElementById('password-input');
		const emailError =document.getElementById('email-error');
		const passwordError =document.getElementById('password-error');
	
	//if user forcus input is set value error empty
	
	emailInput.addEventListener("focus", function() {
		  emailError.textContent = ""; 
		});
	passwordInput.addEventListener("focus", function() {
		passwordError.textContent = ""; 
		});
	
	loginForm.addEventListener('submit', async (event) => {
	  event.preventDefault();
	  
	  const formData = new FormData(loginForm);
	  const email = formData.get('email');
	  const password = formData.get('password');
	  
	  //check username or email is empty
	  function isEmpty(str) {
		  return (!str || str.length === 0);
	  }
	  
		
			const response = await fetch('/login/checkLogin', {
			    method: 'POST',
			    headers: {
			      'Content-Type': 'application/json'
			    },
			    body: JSON.stringify({
			      email: email,
			      password: password
			    })
			  });
			
			//if the authentication is successful, add token in header 
			  if (response.ok) {
				  
				 const data = await response.json();
			     const token = data.token;
			     const role = data.role;
			    alert(token);
			      const homeResponse = await fetch('/product/user-home', {
			        headers: {
			          'Authorization': 'Bearer ' + token
			        }
			      });
			      
			      const homeAdminResponse = await fetch('/order/admin/all', {
				        headers: {
				          'Authorization': 'Bearer ' + token
				        }
				      });
			
			      if (homeResponse.ok) {
			        if (role === 'USER') {
			          window.location.href = "/product/user-home";
			          
			        } else if (role === 'ADMIN') {
			          window.location.href = '/order/admin/all';
			        }
			      } else {
			        alert('Log in failed!');
			      }
			  
			  } else {
				
				const errors = await response.json();
				  
				  // Truy cập vào các trường dữ liệu trong map báo lỗi
				  for (const key in errors) {
					
				    if (errors.hasOwnProperty("EmailErrors")) {
				       
				     
						  emailError.textContent = errors[key];
					  }
					if(errors.hasOwnProperty("PasswordErrors")){
						  passwordError.textContent = errors[key];
					  
				      
				    }
				    if (errors.hasOwnProperty("bindingErrors")) {
		      	            var bindingErrors = errors["bindingErrors"];
		      	            for (var i = 0; i < bindingErrors.length; i++) {
		      	                var error = bindingErrors[i];
		      	                
		      	                if(error.field=="email"){
									  emailError.textContent = error.defaultMessage;
								  }
								  if(error.field=="password"){
									  passwordError.textContent =  error.defaultMessage;
								  }
		      	            }
			      		}
				  }
				//const message=data.message;
			    //alert(message);
			  }
		}
	);