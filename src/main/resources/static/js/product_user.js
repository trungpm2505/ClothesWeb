$(document).ready(function () {
	

  $(".image-slider").slick({
    slidesToShow: 1,
    slidesToScroll: 1,
    infinite: true,
    arrows: true,
    draggable: false,
    prevArrow: `<button type='button' class='slick-prev slick-arrow rounded-circle shadow'><i class="fa fa-chevron-left"></i></button>`,
    nextArrow: `<button type='button' class='slick-next slick-arrow rounded-circle shadow'><i class="fa fa-chevron-right"></i></button>`,
    dots: false,
   initialSlide: 0,
   slidesPerView: 1,
   autoplay: false,
    responsive: [
      {
        breakpoint: 1025,
        settings: {
          slidesToShow: 3,
        },
      },
      {
        breakpoint: 480,
        settings: {
          slidesToShow: 1,
          arrows: false,
          infinite: false,
        },
      },
    ],
    //autoplay: true,
    // autoplaySpeed: 1000,
  });

  
    $('.women-product').on('click', function() {
            window.location.href = '/product/women-fashion';
        });
         $('.men-product').on('click', function() {
            window.location.href = '/product/men-fashion';
        });
         $('.all-product').on('click', function() {
            window.location.href = '/product/all-roduct';
        });
        $('.best-seller').on('click', function() {
            window.location.href = '/product/best-seller';
        });
        
    
	loadData(0,".product-list",3,0,"");
	loadData(0,".women .slick-track",0,0,"");
	loadData(0,".best-sellers .slick-track",3,0,"");
	loadData(0,".men .slick-track",1,0,"");
	
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
		       
				if(container===".best-sellers .slick-track"){
						 data.productResponseDtos.sort(function(a, b) {
							 
						  return b.quantityOfSold- a.quantityOfSold;
						});
					 }
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
    
    //add to cart
   	//add csrf
		/*document.querySelector(".add-cart-form").addEventListener("submit", function(event) {
		  event.preventDefault(); // Ngăn form được submit mặc định
			alert(1);
		  // Lấy giá trị CSRF token từ cookie
		  var csrfToken = Cookies.get('XSRF-TOKEN');
		var csrfInputElement = event.target.querySelector(".csrfToken");
		 alert(csrfInputElement);
		  // Gán giá trị CSRF token vào input
		  if (csrfInputElement) {
			  alert("set csrf");
		    csrfInputElement.value = csrfToken;
		  }
		
		  // Submit form
		  event.target.submit();
		});*/
		
		/*function handleSubmit(event) {
		  event.preventDefault(); // Ngăn form được submit mặc định
		
		  var button = event.target; // Lấy phần tử nút submit được bấm
		  var form = button.closest('.add-cart-form'); // Tìm phần tử form cha có class .add-cart-form
		  var csrfInputElement = form.querySelector('.csrfToken'); // Tìm phần tử có class .csrfToken trong form
			alert("set csrf");
		  // Thực hiện xử lý với phần tử .add-cart-form và .csrfToken ở đây
		  if (form && csrfInputElement) {
		    // Gán giá trị CSRF token vào input
		    var csrfToken = Cookies.get('XSRF-TOKEN');
		    csrfInputElement.value = csrfToken;
		
		    // Submit form
		    form.submit();
		  }
		}*/
		

		
		
});
        
        
        
      

	     