 $(document).ready(function () {
 
	 $('.all-product').on('click', function() {
	            window.location.href = '/product/all-roduct';
	        });
	loadData(".products");

	
	function loadData(container) {
			var productContainer = container;
			
		    $.ajax({
		      url: '/product/getBestSeller',
		      type: 'GET',
		      dataType: 'json',
		  
		      success: function(productResponseDtos) {
		        //clear table
		        $(productContainer).empty();
				
		        //add record
		        $.each(productResponseDtos, function(index, product) {
					 
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
				product_review.append($('<button>').addClass('btn btn-sm add-to-card').attr({'type': 'button','data-product-id': product.id}).text("Add to cart").click(function(event) {
			      addToCart(event)
			    }));
					
				  	
				  	product_desc_info.append(product_review);
				  	
				  	productDesc.append(product_desc_info);
				  	
				  	thumnail_productwrap.append(productDesc);
				  	thumnailProduct.append(thumnail_productwrap);
				  	
						$(productContainer).append(thumnailProduct);
			
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



});
	     