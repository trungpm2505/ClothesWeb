$(document).ready(function () {
	//+- quantity
			
	 function quantityClick(event) {
		    var button = event.target;
		    var input = $(button).closest('.sp-quantity').find("input.quantity-input");
		    var oldValue = input.val(),
		        newVal;
		        
		    if ($.trim($(button).text()) == "+") {
		        newVal = parseFloat(oldValue) + 1;
		      
		    } else {
		        // Don't allow decrementing below zero
		        if (oldValue > 1) {
		            newVal = parseFloat(oldValue) - 1;
		        } else {
		            newVal = 1;
		        }
		      
		    }
		  
		    $(input).val(newVal);
		    updateQuantity($(button).data('cart-id'),newVal);
		  
		};
		//update quantity
		function updateQuantity(cartId,quantity) {
		   
			csrfToken = Cookies.get('XSRF-TOKEN');
			
		    $.ajax({
		      url: '/cart/updateQuantity?cartId='+ parseInt(cartId)+'&quantity='+parseInt(quantity),
		      type: 'PUT',
		      // dataType: 'json',
		      contentType: "application/json",
		      //data: JSON.stringify({ cartId: parseInt(cartId) ,quantity: parseInt(quantity)}),
		       headers: {
				   
			    'X-XSRF-TOKEN': csrfToken
			  },
		      success: function() {
		        
		       
		       
		        loadData();
		      },
		      error: function(jqXHR, textStatus, errorThrown){
				 
		    	  if (jqXHR.status === 400) {
		    	        var error = jqXHR.responseJSON || jqXHR.responseText;
		    	        alert(error);
		    	       
		    	    } else {
		    	        // xử lý lỗi khác
		    	        console.log(textStatus);
		    	        console.log(errorThrown);
		    	    }
		      }})
	        };
	        
	        //delete cart
	        var buttonDelete;
		function deleteProduct(event) {
		   buttonDelete= event.target;
			csrfToken = Cookies.get('XSRF-TOKEN');
			
		    $.ajax({
		      url: '/cart/delete?cartId='+ parseInt($(buttonDelete).data('cart-id')),
		      type: 'DELETE',
		      // dataType: 'json',
		      contentType: "application/json",
		      //data: JSON.stringify({ cartId: parseInt(cartId) ,quantity: parseInt(quantity)}),
		       headers: {
				   
			    'X-XSRF-TOKEN': csrfToken
			  },
		      success: function() {
				   loadData();
		      },
		      error: function(jqXHR, textStatus, errorThrown){
				 
		    	  if (jqXHR.status === 400) {
		    	        var error = jqXHR.responseJSON || jqXHR.responseText;
		    	        alert(error);
		    	       
		    	    } else {
		    	        // xử lý lỗi khác
		    	        console.log(textStatus);
		    	        console.log(errorThrown);
		    	    }
		      }})
	        };
	        
	     //the end cart
		$(window).scroll(function() {
		  var scrollDistance = $(document).height() - $(window).height() - $('.footer').outerHeight() - $('.product-bottom-first').outerHeight();
		
		  if ($(window).scrollTop() >= scrollDistance) {
		    $('.main.product-bottom-wrap').fadeOut();
		  } else {
		    $('.main.product-bottom-wrap').fadeIn();
		  }
		});
		
		//get current page to load page when delete or update a record
		var currentPageForEditAndDelete;
		$('table tbody').on('click', 'tr', function() {
			var firstRow = $(this).closest('tbody').find('tr:first');
			var startIndex = firstRow.children('td:first').text();
			currentPageForEditAndDelete = Math.ceil((startIndex - 1) / size);
		});

	loadData();

	var formattedPrice;
	var formattedCurentPrice;
	var totalProduct;
	function loadData() {
			var cartContainer = $(".cart-container");
			
		    $.ajax({
		      url: '/cart/findAll',
		      type: 'GET',
		      dataType: 'json',
		      contentType: "application/json",
		      //data: {size : 30,page : page},
		      success: function(cartResponseDtos) {
		        //clear 
		        $(cartContainer).empty();
				
		        //add record
		        $.each(cartResponseDtos, function(index,cartResponseDto) {
					
			
					  var productSection = $('<div>').addClass('product-section').append($('<input>').addClass("product-id").attr('type', 'hidden').val(cartResponseDto.productResponseDto.id))
					  ;
					
					  // product top
					  var productTop = $('<div>').addClass('product-top');
					
					  // product left
					  var productLeft = $('<div>').addClass('product-left');
					
					  // check box
					  var inputElement = $('<input>').css('float', 'left').addClass('choose-product').attr({
					    type: 'checkbox',
					    name: 'choose',
					   'data-cart-id' : cartResponseDto.cartId
					  });
					
					  productLeft.append(inputElement);
					
					  // image
					  var imageElement = $('<img>').addClass("image-cart").attr('src', '../upload/' + cartResponseDto.images.inmageForSave).on('click', function(event) {
						  // Xử lý khi phần tử được click
						 redirectToDetail(event);
						});
					  productLeft.append(imageElement);
					
					  productTop.append(productLeft);
					
					  // product right
					  var productRight = $('<div>').addClass('product-right');
					  productRight.append($('<h2>').text(cartResponseDto.productResponseDto.title).on('click', function(event) {
						  // Xử lý khi phần tử được click
						 redirectToDetail(event);
						}));
					
					  var ul = $('<ul>').addClass('size');
					
					  if (cartResponseDto.productAttributeValue != null) {
					    if (cartResponseDto.productAttributeValue.color != null) {
					      ul.append($('<li>').text(cartResponseDto.productAttributeValue.color));
					    }
					    if(cartResponseDto.productAttributeValue.color != null && cartResponseDto.productAttributeValue.size != null){
							ul.append($('<li>').text("|"));
						}
					    if (cartResponseDto.productAttributeValue.size != null) {
					      ul.append($('<li>').text(cartResponseDto.productAttributeValue.size));
					    }
					  }
					
					  productRight.append(ul);
					  productTop.append(productRight);
					  productSection.append(productTop);
					
					  // product right1
					   formattedPrice =cartResponseDto.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
					   
					  var productRight1 = $('<div>').addClass('product-right1');
					  var price = $('<div>');
					  if(cartResponseDto.currentPrice!=null){
						  formattedCurentPrice =cartResponseDto.currentPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
						price.append($('<span>').addClass('new-price').text(formattedCurentPrice));
					  	price.append($('<span>').addClass('ml-5px old-price').text("-"+formattedPrice));
					  }else if(cartResponseDto.currentPrice==null){
						  price.append($('<span>').addClass('ml-5px new-price').text(formattedPrice));
					  }
					  
					  productRight1.append(price);
					  productSection.append(productRight1);
					  
					  //spQuantity 
					  var spQuantity = $('<div>').addClass('sp-quantity d-flex align-items-center mt-60');
					  spQuantity.append( $('<div>').addClass("sp-minus fff").append($("<a>").addClass("ddd").attr({'href': 'javascript:void(0)','data-multi':'-1','data-cart-id' : cartResponseDto.cartId}).text("-").on('click', function(event) {
						  // Xử lý khi phần tử được click
						  quantityClick(event);
						})));
					  spQuantity.append( $('<div>').addClass("sp-input").append($('<input>').attr('type', 'text').addClass('quantity-input').val(cartResponseDto.quantity).on('blur', function(event) {
						  // Xử lý khi phần tử được click
						  updateQuantity(cartResponseDto.cartId,event.target.value) ;
						})));
					  spQuantity.append( $('<div>').addClass("sp-plus fff").append($("<a>").addClass("ddd").attr({'href': 'javascript:void(0)','data-multi':'1','data-cart-id' : cartResponseDto.cartId}).text("+").on('click', function(event) {
						  // Xử lý khi phần tử được click
						  quantityClick(event);
						})
						));
											  
					  productSection.append(spQuantity);
					
					  // total product
					  totalProduct=cartResponseDto.total.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
					  var totalProductDiv = $('<div>').addClass('total-product');
					  totalProductDiv.attr('data-value',cartResponseDto.total).append($('<p>').text(totalProduct));
					  productSection.append(totalProductDiv);
					
					  // close
					  var close = $('<div>').addClass('close').append($('<span>').append($('<i>').addClass('fa fa-trash-o').attr({'data-cart-id' : cartResponseDto.cartId}).on('click', function(event) {
						  // Xử lý khi phần tử được click
						  deleteProduct(event);
						})));
					  productSection.append(close);
					
					  // add product into cart container
					  cartContainer.append(productSection);
					
			
		        });
		      	//pagination = '';
		         //currentPage = productPageResponseDto.currentPage;
		         //totalPages = productPageResponseDto.totalPages;
		         //size = productPageResponseDto.size;
		        //create pagination 
		       //createPagination("#paging",pagination,currentPage,totalPages );
		      },
		      error: function(){}})}
  
		  	//bắt sự kiện đến trang chi tiết 
		  	var thumbnailProductWrap;
		  	var id;
		  	function redirectToDetail(event) {
				  thumbnailProductWrap = event.currentTarget.closest('.product-section');
				 
				  id =   thumbnailProductWrap.querySelector('.product-id').value;
				 
		        window.location.href = '/product/details?productId='+ id ;
		    }
		
		    
			  //function to create pagination after call ajax
	      function createPagination(navId,pagination,currentPage,totalPages ){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +')">' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +')" >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1) +')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
		
		
		//check box
		var subTotalValue ;
		var total =0;
		var totalMoney = document.querySelector('.total-money');
		var productQuantity=document.querySelector('.quantityTotal');
		var totalMoney1 = document.querySelector('.total-money1');
		var productQuantity1=document.querySelector('.quantityTotal1');
		var value=0;
		var quantity=0;
		$(document).on("change",".product-wrap input[type='checkbox']",function() {
			 subTotalValue = parseInt($(this).closest('.product-wrap').find('.total-product').text());
            if($(this).attr('class') === 'check-all') {
				total=0;
                if($(this).is(':checked')) {
					 $("input[name='choose']").prop('checked', true);
					 $("input[class='check-all']").prop('checked', true);
					 subTotalValue = document.getElementsByClassName('total-product');
					 
					 Array.from(subTotalValue).forEach(function(element) {
						 quantity++;
					   value = parseInt($(element).data('value'));
					    total += value;
					});
					totalMoney.textContent = total;
					productQuantity.textContent = quantity;
					totalMoney1.textContent = total;
					productQuantity1.textContent = quantity;
                   
                } else {
                    $("input[name='choose']").prop('checked', false);
                     $("input[class='check-all']").prop('checked', false);
                    quantity=0;
                     total=0;
                     totalMoney.textContent = 0;
                     productQuantity.textContent = 0;
                     totalMoney1.textContent = 0;
					productQuantity1.textContent = 0;
                    
                }
            } else {
                if($("input[name='choose']").length == $("input[name='choose']:checked").length) {
					
                    $(".check-all").prop('checked', true);
                } else {
					
                    $(".check-all").prop('checked', false);
                }
            }
            
            if($(this).hasClass('choose-product')){
				subTotalValue = parseInt($(this).closest('.product-section').find('.total-product').data('value'));
				
				if (this.checked) {
			      // Checkbox được chọn
			      quantity++;
				  total+=subTotalValue;
			    } else {
					quantity--;
			      // Checkbox không được chọn
			      total-=subTotalValue;
			    }
			    totalMoney.textContent = total.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
			    
			    productQuantity.textContent = quantity;
			    
			    totalMoney1.textContent = total.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
				productQuantity1.textContent = quantity;
			}
            
           // totalMoney.textContent = total;

           /* if($("input[name='choose']:checked").length > 0) {
                $("#delete-all").prop('disabled', false);
            } else {
                $("#delete-all").prop('disabled', true);
            }*/
        });
        
        function getSelectedProducts() {
			var cartIdList = [];
		  var checkboxes = document.querySelectorAll('.product-section input[type="checkbox"]:checked');
		  
		  for (var i = 0; i < checkboxes.length; i++) {
			  cartIdList.push(checkboxes[i].getAttribute('data-cart-id'));
			
		  }
		 if(cartIdList.length==0){
			 $("#itemEmpty").modal("show");
			 return false;
		 }		  
		  window.location.href = "/order/checkout?cartIdList="+cartIdList;
		}
		
		//var checkoutButton = document.getElementById('checkoutBtn');
 		//checkoutButton.addEventListener('click', getSelectedProducts);
 		
 		var checkoutButtons = document.getElementsByClassName("checkoutBtn");
        for (var i = 0; i < checkoutButtons.length; i++) {
            checkoutButtons[i].addEventListener('click', getSelectedProducts);
        }
	
		
		
});