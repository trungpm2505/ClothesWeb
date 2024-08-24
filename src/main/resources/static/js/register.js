const confirmPassword = document.getElementById('confirmPassword');
const confirmPasswordErrors = document.getElementsByName('password-error');
confirmPassword.addEventListener("focus", function() {
	confirmPasswordErrors.forEach(function(error) {
		error.textContent = '';
	});
});

const inputList = ['fullname', 'username', 'password', 'address', 'email', 'phone'];


inputList.forEach(function(inputName) {
	
  const input = document.getElementById(inputName);
  const errors = document.getElementsByName(inputName + '-error');
  input.addEventListener("focus", function() {
	  errors.forEach(function(error) {
		  error.textContent = '';
		});
  });
});