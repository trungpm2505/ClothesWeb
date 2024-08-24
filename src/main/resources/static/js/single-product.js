$(document).ready(function () {

	
	$('.image-slider').slick({
		slidesToShow: 1,
        slidesToScroll: 1,
  		infinite: false
});
	
  
  
  
  
  
  //kích vào see more
   $('.all-product').on('click', function() {
            window.location.href = '/product/all-roduct';
        });
        
    //load product list
  	loadData(0,".product-list",3,0,"");
	

 	function loadData(page,container,type, categoryId, keyword) {
			var productContainer = container;
			
		    $.ajax({
		      url: '/product/getProductPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {size : 25,page : page,type:type,categoryId : categoryId,keyword:keyword},
		      success: function(data) {
		        //clear table
		        $(productContainer).empty();
				
		        //add record
		        $.each(data.productResponseDtos, function(index, product) {
					 
					var thumnailProduct = $('<div>').addClass('thumnail-product');
					var thumnail_productwrap = $('<div>').addClass('thumnail-product-wrap').click(redirectToDetail);
				
					//add id
					thumnail_productwrap.append($('<div>').text(product.id).hide().addClass('id'));
					//add image
					$.each(product.images, function(i,image) {
						//imageNames.push(product.images[i].inmageForSave);
  						if (product.images[i].isDefault == true) {
							  
							  thumnail_productwrap.append(
							  $('<div>').addClass('product-image').append(
							    $('<img>').attr('src', "../upload/"+image.inmageForSave).addClass('default-image').click(redirectToDetail)
							  )
							);
				  		}
				  	});
				  	//add hr
				  	//thumnail_productwrap.append($('<hr>').addClass('hr-line'))
				  	//add details
				  
				  	var productDesc = $('<div>').addClass('product_desc');
				  	var product_desc_info = $('<div>').addClass('product_desc_info').click(redirectToDetail);
				  	product_desc_info.append($('<span>').text(product.title).addClass('product_name '));
				  	
				  	var product_review = $('<div>').addClass('product-review').click(redirectToDetail);
				  	//price box
				  	var price_box = $('<div>').addClass('price-box').click(redirectToDetail);
				  	var formattedPrice = product.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
				  	
		            if( product.currentPrice!=null){
						  var formattedCurentPrice =product.currentPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
						  price_box.append($('<span>').text(formattedCurentPrice).addClass('new-price text-danger').click(redirectToDetail));
						  price_box.append($('<span>').text(formattedPrice).addClass('old-price').click(redirectToDetail));
					  }	else{
						  price_box.append($('<span>').text(formattedPrice).addClass('new-price text-danger').click(redirectToDetail));
					  }
				  	
				  	product_review.append(price_box);
					
					//manufacturer
					var manufacturer = $('<h5>').addClass('manufacturer');
					manufacturer.append($('<span>').text(product.quantityOfSold+' sold').addClass('new-price').click(redirectToDetail));
					
					product_review.append(manufacturer);
					
					//button add cart
					//add id
					product_review.append($('<button>').addClass('btn btn-sm add-to-card').attr({'type': 'button','data-product-id': product.id}).text('Add to cart').click(function(event) {
			      addToCart(event)
			    }));
					

				  	product_desc_info.append(product_review);
				  	
				  	productDesc.append(product_desc_info);
				  	
				  	thumnail_productwrap.append(productDesc);
				  	thumnailProduct.append(thumnail_productwrap);
				  	if(productContainer!='.product-list'){
						  $(productContainer).append($('<div>').addClass('image-item slick-slide slick-cloned')
						  .append(thumnailProduct));
							
						 //$(productContainer).addClass('product-active owl-carousel owl-loaded owl-drag');
					}else{
						$(productContainer).append(thumnailProduct);
					}
				  	
				  	
				  	
		        });
		      
		      },
		      error: function(){}})}
		      
		      
		      
		      loadRate(0,0);
	
		//load rating
		
		var currentPage ;
		var totalPages ;
		var size;
		var pagination;
		function loadRate(page,rating) {
			var productContainer = '.shopee-product-comment-list';
			var productId = $('#product-id-single-product').val();
			
		    $.ajax({
		      url: '/rate/getRatePage',
		      type: 'GET',
		      dataType: 'json',
		      data: {page : page, productId : productId,rateScore : rating},
		      success: function(data) {
		        //clear table
		        $(productContainer).empty();
				
		        //add record
		        $.each(data.rateResponseDto, function(index, rate) {
					 
					var thumnailRate = $('<div>').addClass('shopee-product-rating');
					//avatar
					var shopeeProductRatingAvatar = $('<div>').addClass('shopee-product-rating__avatar');
					
					var shopeeAvatar = $('<div>').addClass('shopee-avatar').append($('<img>').attr('src', "https://down-vn.img.susercontent.com/file/6eaf8c444136137db7cb2a03e7c49046_tn").addClass('shopee-avatar__img'));
					shopeeProductRatingAvatar.append(shopeeAvatar);
					thumnailRate.append(shopeeProductRatingAvatar);
					
					//phaanf contend
					var shopeeProductRatingMain = $('<div>').addClass('shopee-product-rating__main');
					//name
					 shopeeProductRatingMain.append($('<div>').addClass('shopee-product-rating__author-name').text(rate.userResponseDto.userName));
					//star
					var  repeatPurchaseCon=  $('<div>').addClass('repeat-purchase-con');
					var  shopeeProductRatingRating=  $('<div>').addClass('shopee-product-rating__rating');
					var ratingUl = $('<ul>').addClass('rating rating-with-review-item');

					for(var i=1;i<=5;i++){
						
						if(i <= rate.rating){
							ratingUl.append($('<li>').append($('<i>').addClass('fas fa-star yellowStar')))
						}else{
							ratingUl.append($('<li>').append($('<i>').addClass('fas fa-star grayStar')))
						}
						
					}
					
					shopeeProductRatingRating.append(ratingUl);
					repeatPurchaseCon.append(shopeeProductRatingRating);
					shopeeProductRatingMain.append(repeatPurchaseCon);
					
					//infor sp
					
					var  shopeeProductRatingTime=  $('<div>').addClass('shopee-product-rating__time').text(rate.createAt);
					shopeeProductRatingMain.append(shopeeProductRatingTime);
					
					//content rate
					var  Rk6V3=  $('<div>').addClass('Rk6V+3').text( rate.content!=''?rate.content : 'The customer did not leave any comments.');
					shopeeProductRatingMain.append(Rk6V3);
					var imageList=$('<div>').addClass('imageList');

					for(var i =0;i<rate.images.length;i++){
						
						  imageList.append($('<img>').attr('src', "../upload/"+rate.images[i].inmageForSave).addClass('thumbnail'))
						
					}
					shopeeProductRatingMain.append(imageList);
					//response
					if(rate.responses.length!=0){
						var  fwJamt =  $('<div>').addClass('fwJamt');
						var  response;

						for(var i =0;i<rate.responses.length;i++){
							  response=  $('<div>').addClass('response');
							if(rate.responses[i].userResponseDto.roleName=="ADMIN"){
								response.append($('<div>').addClass('CaoTou').text("Puu Shop"));
							}else{
								response.append($('<div>').addClass('CaoTou').text(rate.responses[i].userResponseDto.userName));
							}
							response.append($('<div>').addClass('_2kece8').text(rate.responses[i].content));
							fwJamt.append(response);
						}
						
						
						shopeeProductRatingMain.append(fwJamt);
					}
					
					
				shopeeProductRatingMain.append($('<button>').addClass('btn btn-sm btn-response').attr({'type': 'button'}).text('Comment').click(function(event) {
			      opentResponse(event);
			    }))
					shopeeProductRatingMain.append($('<button>').addClass('btn btn-sm btn-close-response hide').attr({'type': 'button'}).text("close").click(function(event) {
			      clickCLoseResponse(event);
			    }));
				 var div = ($('<div>').addClass('d-flex contain-new-response'));
				 div.append($('<textarea>').addClass('content-response hide'));
				 div.append($('<button>').addClass('btn-save-response hide').attr({'type': 'button','data-rate-id': rate.id}).text("Sent").click(function(event) {
			      saveResponse(event);
			    }));
				 
				 shopeeProductRatingMain.append(div);
				thumnailRate.append(shopeeProductRatingMain);
				  $(productContainer).append(thumnailRate);
				  	
		        });
		      	 pagination = '';
		         currentPage = data.currentPage;
		         totalPages = data.totalPages;
		         size = data.size;
		        //create pagination 
		       createPagination("#paging",pagination,currentPage,totalPages);
		       
				var pagingElements = $(".page-link");

				for (var i = 0; i < pagingElements.length; i++) {
				
					  var pagingElement = pagingElements[i];
					
					  $(pagingElement).click(function() {
					
					    var dataNumber = $(this).attr("data-number");
					    if(parseInt(dataNumber)>=0){
							loadRate(parseInt(dataNumber),rating);
						}
					  });	
				}
		      },
		      error: function(){}})}


  			//function to create pagination after call ajax
	      function createPagination(navId,pagination,currentPage,totalPages){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled li-paging"') + '><a class="page-link" data-number=' + (currentPage > 0 ? (currentPage - 1)  : -1) + ' aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item  li-paging"' : 'class=" li-paging"') + '><a class="page-link" data-number=' + i +'>' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li class="li-paging"><a class="page-link" data-number=' + (totalPages - 1) +' >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? 'class=" li-paging"' : 'class="page-item disabled li-paging"') + '><a class="page-link" data-number=' + (currentPage < totalPages - 1 ? (currentPage + 1) : -1) + ' aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
  			
  			function opentResponse(event){
				 
				 $(event.target).closest('.shopee-product-rating__main').find('.btn-response').addClass("hide");
				 $(event.target).closest('.shopee-product-rating__main').find('.btn-close-response').removeClass("hide");
				 $(event.target).closest('.shopee-product-rating__main').find('.content-response').removeClass("hide");
				 $(event.target).closest('.shopee-product-rating__main').find('.btn-save-response').removeClass("hide");
				 
			}
			function clickCLoseResponse(event){
				$(event.target).closest('.shopee-product-rating__main').find('.btn-response').removeClass("hide");
				 $(event.target).closest('.shopee-product-rating__main').find('.btn-close-response').addClass("hide");
				 $(event.target).closest('.shopee-product-rating__main').find('.content-response').addClass("hide");
				  $(event.target).closest('.shopee-product-rating__main').find('.btn-save-response').addClass("hide");
				
			}
			
			var csrfToken;
	
    function saveResponse(event) {

       
			
			  csrfToken = Cookies.get('XSRF-TOKEN');
			  
			  // Kiểm tra xem token có tồn tại hay không
			  if (csrfToken) {
			    // Tạo đối tượng dữ liệu để gửi qua Ajax
			   
			    var data = {
				  rateId: $(event.target).attr('data-rate-id'),
				  content: $(event.target).closest('.shopee-product-rating__main').find('.content-response').val()
				};
			    // Gửi Ajax request
			    $.ajax({
			      url: '/response/add',
			      type: 'POST',
			      contentType: "application/json",
			      data: JSON.stringify(data),
			      headers: {
			        'X-XSRF-TOKEN': csrfToken
			      },
			      success: function(response,textStatus, jqXHR) {
					
				    var contentType = jqXHR.getResponseHeader("Content-Type");
				    if (contentType.includes("text/html")) {
						 
				      // Redirect the user to the login page
				     window.location.href = "/login";
				    } else{
						
	                	loadRate(currentPage,$('.search-rating.selected').attr('data-rating'));
					}
			      },
			      error: function(xhr, status, error) {
				            alert("The system has errors!");
			      }
			    });
			  } 
    }
        var searchRating = document.getElementsByClassName('search-rating');
		for (var i = 0; i < searchRating.length; i++) {
			
				searchRating[i].addEventListener('click', function(event) {
    			changeColor(event.target);

		  });
		        }
		function changeColor(event) {
            var filters = document.getElementsByClassName('product-rating-overview__filter');
            for (var i = 0; i < filters.length; i++) {
                filters[i].classList.remove('selected');
            }
            event.closest('.product-rating-overview__filter').classList.add('selected');
            
            loadRate(0,event.getAttribute('data-rating'));
        }
/*var csrfToken;
			function saveResponse(event){
				csrfToken = Cookies.get('XSRF-TOKEN');
				alert($(event.target).attr('data-rate-id'));	
				alert($(event.target).closest('.shopee-product-rating__main').find('.content-response').val());
				//var formData = new FormData();
				
                //formData.append('rateId',  $(event.target).attr('data-rate-id'));  
                //formData.append('content', $(event.target).closest('.shopee-product-rating__main').find('.content-response').val()); 
                var requestData = {
				  rateId: $(event.target).attr('data-rate-id'),
				  content: $(event.target).closest('.shopee-product-rating__main').find('.content-response').val()
				};
                $.ajax({
		        url: '/response/add',
		        type: 'POST',
		        //contentType: false,
		        //processData: false,
		        contentType: 'application/json',
//data: formData,
				//data: JSON.stringify({rateId: $(event.target).attr('data-rate-id'),content : $(event.target).closest('.shopee-product-rating__main').find('.content-response').val()}),
				data:JSON.stringify(requestData),
		        dataType: 'json',
		        cache: false,
		         headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
		        success: function(response,textStatus, jqXHR) {
					var contentType = jqXHR.getResponseHeader("Content-Type");
				    if (contentType.includes("text/html")) {
						 
				      // Redirect the user to the login page
				     window.location.href = "/login";
				    } 
					alert("thành công")
		        	loadRate(0);
		        	
		        },
		        error: function(xhr, status, error) {
					 alert("lõi"+textStatus);
		             	if (jqXHR.status === 400) {
						window.location.href = "/login";
					}
		        	}
		        	
		    	});   
			}*/
		  	//bắt sự kiện đến trang chi tiết 
		  	var thumbnailProductWrap;
		  	var id;
		  	function redirectToDetail(event) {
				  thumbnailProductWrap = event.currentTarget.closest('.thumnail-product-wrap');
				 
				  id = thumbnailProductWrap.querySelector('.id').textContent;
				 
		        window.location.href = '/product/details?productId='+ id ;
		    }
		
		    var id;
	var csrfToken;
	var productId;
    function addToCart(event) {
        event.stopPropagation();
       
			 productId=$(event.target).data('product-id');
			  csrfToken = Cookies.get('XSRF-TOKEN');
			  
			  // Kiểm tra xem token có tồn tại hay không
			  if (csrfToken) {
			    // Tạo đối tượng dữ liệu để gửi qua Ajax
			    var data = {
			      productId: productId
			    };
			    
			    // Gửi Ajax request
			    $.ajax({
			      url: '/cart/save',
			      type: 'POST',
			      contentType: "application/json",
			      data: JSON.stringify(data),
			      headers: {
			        'X-XSRF-TOKEN': csrfToken
			      },
			      success: function(response,textStatus, jqXHR) {
					
				    var contentType = jqXHR.getResponseHeader("Content-Type");
				    if (contentType.includes("text/html")) {
						 
				      // Redirect the user to the login page
				     window.location.href = "/login";
				    } else{
						$('#successMessage').fadeIn();

	                	setTimeout(function() {
	                	  $('#successMessage').fadeOut();
	                	}, 2000);
					}
			      },
			      error: function(xhr, status, error) {
					  
				            alert("The system has errors!");
			      }
			    });
			  } 
    }
		    



			
			//+- quantity
			
			 $(".ddd").on("click", function () {
				    var $button = $(this),
				        $input = $button.closest('.sp-quantity').find("input.quantity-input");
				    var oldValue = $input.val(),
				        newVal;
				    if ($.trim($button.text()) == "+") {
				        newVal = parseFloat(oldValue) + 1;
				    } else {
				        // Don't allow decrementing below zero
				        if (oldValue > 1) {
				            newVal = parseFloat(oldValue) - 1;
				        } else {
				            newVal = 1;
				        }
				    }
				    $input.val(newVal);
				    var price = $('.total_amount').data('price');
				    $('.total_amount').html(price * newVal);
				});
				
		
		
		
		
		//add csrf
		/*document.getElementById("add-to-cart-single").addEventListener("submit", function(event) {
		  event.preventDefault(); // Ngăn form được submit mặc định
		
		  // Lấy giá trị CSRF token từ cookie
		  var csrfToken = Cookies.get('XSRF-TOKEN');
		
		  // Gán giá trị CSRF token vào input
		  document.getElementById("csrfToken").value = csrfToken;
		
		  // Submit form
		  this.submit();
		});*/


	var quantity;
	var csrfToken;
	var productId;
    function addToCartInSingleProduct(event) {
        event.stopPropagation();
			 productId=document.getElementById("product-id-single-product").value;
			 quantity=document.getElementById("quantity-single-product").value;
			  csrfToken = Cookies.get('XSRF-TOKEN');
			  
			  // Kiểm tra xem token có tồn tại hay không
			  if (csrfToken) {
			    // Tạo đối tượng dữ liệu để gửi qua Ajax
			    var data = {
			      productId: productId,
			      number : quantity
			    };
			    
			    // Gửi Ajax request
			    $.ajax({
			      url: '/cart/save',
			      type: 'POST',
			      contentType: "application/json",
			      data: JSON.stringify(data),
			      headers: {
			        'X-XSRF-TOKEN': csrfToken
			      },
			      success: function(response,textStatus, jqXHR) {
					
				    var contentType = jqXHR.getResponseHeader("Content-Type");
				    if (contentType.includes("text/html")) {
						
				      // Redirect the user to the login page
				      window.location.href = "/login";
				    } else{
						$('#successMessage').fadeIn();

	                	setTimeout(function() {
	                	  $('#successMessage').fadeOut();
	                	}, 2000);
					}
			      },
			      error: function(xhr, status, error) {
				       alert("The system has errors!");
			      }
			    });
			  } 
    }
     $("#add-to-cart-single").click(addToCartInSingleProduct);
 	


		
	     
});
document.addEventListener('DOMContentLoaded', function() {
  var imagesCount = $('.carousel1').find('.thumbnail').length;
  var slidesToShow = imagesCount >= 4 ? 3 : imagesCount;

  $('.carousel1').slick({
    slidesToShow: slidesToShow,
    dots: true,
    centerMode: imagesCount > 3,
    centerPadding: '0px'
  });

  var thumbnails = document.querySelectorAll('.thumbnail');
  var mainImage = document.querySelector('.main-image');

  thumbnails.forEach(thumbnail => {
    thumbnail.addEventListener('mouseover', () => {
      var thumbnailSrc = thumbnail.getAttribute('src');
      mainImage.setAttribute('src', thumbnailSrc);
    });
  });
});
