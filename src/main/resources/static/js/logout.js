//logout
		var csrfToken = null;
		document.getElementById("logout-form").addEventListener("submit", function(event) {
		  event.preventDefault(); // Ngăn form được submit mặc định
		  // Gửi yêu cầu AJAX
		  var xhr = new XMLHttpRequest();
		  xhr.open("POST", "/logout", true);
		  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		  if (Cookies.get('XSRF-TOKEN')) {
			  csrfToken = Cookies.get('XSRF-TOKEN');
			  xhr.setRequestHeader("X-XSRF-TOKEN", csrfToken);
			}
		  
			
		  xhr.onload = function() {
		     if (xhr.status === 200) {
				 
			      // Đăng xuất thành công, ẩn class "user"
			      //var userElement = document.getElementsByClassName("ht-user")[0];
			      //var userName = document.getElementsByClassName("userName")[0];
			      
			      //userElement.classList.add("hide");
			      //userName.classList.add("hide");
			   
			    } 
		  };
		
		  xhr.send(new FormData(event.target));
		});
