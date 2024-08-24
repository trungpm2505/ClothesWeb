$(document).ready(function () {
var elements = document.getElementsByClassName("total-product");
var total = 0;
var totalMoneyElement = document.querySelector(".total-money");
var subTotal = document.querySelector(".sub-total");

function calculateTotal() {
  for (var i = 0; i < elements.length; i++) {
    var value = parseFloat(elements[i].getAttribute('data-total'));
    total += value;
  }
 
  subTotal.textContent= total.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });;
  total += 30000;
  totalMoneyElement.textContent = total.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });;

}
calculateTotal();
var cartIds = [];

// Lặp qua các input có lớp cartId và lấy giá trị của chúng
var inputElements = document.getElementsByClassName('cartId');

//info
		 var csrfToken;
		function add() {
		   	var fullName = $(".fullName").val();
			var phone = $(".phone-input").val();
			var address = $(".address").val();
			var note = $(".note").val();
			for (var i = 0; i < inputElements.length; i++) {
			 var cartId = parseInt(inputElements[i].value);
			  if (!isNaN(cartId)) {
			    cartIds.push(cartId);
			  }
			}
			
		    csrfToken = Cookies.get('XSRF-TOKEN');
			var data = {
			  fullName: fullName,
			  phone: phone,
			  address: address,
			  cartIds: cartIds,
			  note:note
			};
		    $.ajax({
		      url: '/order/add',
		      type: 'POST',
		     
		      contentType: "application/json",
		      data: JSON.stringify(data),
		       headers: {
				   
			    'X-XSRF-TOKEN': csrfToken
			  },
		      success: function() {
		        
		        window.location.href = "/order/order-success";
		      },
		      error: function(jqXHR, textStatus, errorThrown){
		    	  if (jqXHR.status === 400) {
	      	       var errors = jqXHR.responseJSON;
	      	        if (errors.hasOwnProperty("bindingErrors")) {
	      	            var bindingErrors = errors["bindingErrors"];
	      	            for (var i = 0; i < bindingErrors.length; i++) {
	      	                var error = bindingErrors[i];
						    // Hiển thị thông báo lỗi trong tag ID đã tạo
						    $("#" + error.field + "-error").text(error.defaultMessage);
	      	            }
	      	        }
	      	        if (errors.hasOwnProperty("fileErrors")) {
					    var fileError = errors["fileErrors"];
					    $("#file-input-error").text(fileError);
					}
					if (errors.hasOwnProperty("titleDuplicate")) {
					    var titleError = errors["titleDuplicate"];
					    $("#title-error").text(titleError);
					}
	      	    }else{
					   alert("Sorry! The system have errors!") 
				  }
		      }})
	        };
	         $(".order").click(function() {
				 add();
			 });
})
	     /* var formattedCurrentPrice;
	      
	      var cartContainer = $('<div>').addClass('cart-container');
	      cartContainer.empty();
	      var cartResponseDtos = document.querySelector('.productList').value;
	     // var cartResponseDtoString = cartResponseDtos.substring(1, cartResponseDtos.length - 1); 
	      // Chuyển đổi chuỗi thành mảng
		  var cartResponseArray = cartResponseDtos.split(',');

	      cartResponseArray.forEach(function(index,cartResponseDto) {
			
			 var productSection = $('<div>').addClass('product-section');
			 //top
			 var productTop = $('<div>').addClass('product-top');
			 var cartId = $('<input>').addClass('cartId').val(cartResponseDto.cartId);
			  alert(cartResponseDto.toString().cartId);
			 productTop.append(cartId);
			 //product-left
			 alert(cartResponseDto.images);
			 var productLeft = $('<div>').addClass('product-left').append($('<img>').attr('src', '../upload/' + cartResponseDto.images.inmageForSave));
			 productTop.append(productLeft);
			 //product-right
			 var productRight = $('<div>').addClass('product-right');
			 productRight.append($('<h2>').text(cartResponseDto.title));
			 //ul
			 var ul = $('<ul>').addClass('size d-flex');
			 ul.append( $('<li>').addClass('mr-5').text(cartResponseDto.productAttributeValue.size));
			 ul.append( $('<li>').addClass('mr-5').text(cartResponseDto.productAttributeValue.color));
			 productRight.append(ul);
			 productTop.append(productRight);
			 productSection.append(productTop);
			 
			 
			 //product-right1 d-flex 
			 var productRight1 = $('<div>').addClass('product-right1 d-flex');
			 if(cartResponseDto.currentPrice != null){
				 formattedCurrentPrice =cartResponseDto.currentPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
				 productRight1.append($('<div>').text(formattedCurrentPrice)) ;
			 }else
			 if(cartResponseDto.currentPrice == null){
				 formattedCurrentPrice =cartResponseDto.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
				 productRight1.append($('<div>').text(formattedCurrentPrice)) ;
			 }
			 productRight1.append($('<div>').text(cartResponseDto.quantity)) ;
			 
			 productRight1.append($('<div>').addClass(total-product).attr({
		            	'data-total': cartResponseDto.total
		            }).text(cartResponseDto.total.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }))) ;
		     productSection.append(productRight1);
			 cartContainer.append(productSection);
			});*/
	        

/*document.getElementById('orderForm').addEventListener('submit', function(event) {
  
	alert("vào method order");
    var form = event.target;
    var formData = new FormData(form);

    var csrfToken = Cookies.get('XSRF-TOKEN');

    var xhr = new XMLHttpRequest();
    xhr.open(form.method, form.action);
    xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken);
    xhr.onload = function() {
      // Xử lý phản hồi từ server
    };
    xhr.send(formData);
  });*/
  //lấy mảng cartId


// Gán giá trị mảng cartIds cho input có name="cartIds"
//document.getElementsByName("cartIds")[0].value = cartIds.join(",");


