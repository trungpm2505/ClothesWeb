$('.women-product').on('click', function() {
            window.location.href = '/product/women-fashion';
        });
loadData(0,".products",0,0,"");

	 	function loadData(page,container,type, categoryId, keyword) {
			var productContainer = container;
			
		    $.ajax({
		      url: '/product/getProductPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {size : 30,page : page,type:type,categoryId : categoryId,keyword:keyword},
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
					product_review.append($('<button>').addClass('btn btn-sm add-to-card').attr({'type': 'button','data-product-id': product.id}).text('Add to cart').click(function(event) {
			      addToCart(event)
			    }));
					
				  	
				  	product_desc_info.append(product_review);
				  	
				  	productDesc.append(product_desc_info);
				  	
				  	thumnail_productwrap.append(productDesc);
				  	thumnailProduct.append(thumnail_productwrap);
				  	
						$(productContainer).append(thumnailProduct);
			
		        });
		      	pagination = '';
		         currentPage = data.currentPage;
		         totalPages = data.totalPages;
		         size = data.size;
		        //create pagination 
		       createPagination("#paging",pagination,currentPage,totalPages,container,type, categoryId, keyword  );
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
		
		    
			 
			  //function to create pagination after call ajax
	      function createPagination(navId,pagination,currentPage,totalPages ,container,type, categoryId, keyword){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +',\''+container +'\','+type +','+categoryId +',\''+keyword+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +',\''+container +'\','+type +','+categoryId +',\''+keyword+'\')">' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +',\''+container +'\','+type +','+categoryId +',\''+keyword+'\')" >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1) +',\''+container +'\','+type +','+categoryId +',\''+keyword+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
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