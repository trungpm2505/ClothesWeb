var size;
	//get current page to load page when delete or update a record
	var currentPageForEditAndDelete;
	$('table tbody').on('click', 'tr', function() {
		var firstRow = $(this).closest('tbody').find('tr:first');
		var startIndex = firstRow.children('td:first').text();
		currentPageForEditAndDelete = Math.ceil((startIndex - 1) / size);
	});
	
	var thumbnailOrder;
  	var id;
  	function redirectToDetail(event) {
		  thumbnailOrder = event.currentTarget.closest('.card');
		 
		  id = thumbnailOrder.querySelector('.order-id').textContent;
		 
        window.location.href = '/orderDetails/get?orderId='+ id ;
    }
    function updateRateView(){
		$('#save-rating').removeClass("hide");
		$('#update-rating').addClass("hide");
		var X3Qjvs = $('.X3Qjvs');
		
		for (var i = 0; i < X3Qjvs.length; i++) {
		 
		  var content = $(X3Qjvs[i]).find('._2LhMgE').text();
		  
			
			$(X3Qjvs[i]).empty();
			$(X3Qjvs[i]).append($('<div>').addClass('CLFTQP'));
			$(X3Qjvs[i]).append($('<div>').css('position', 'relative').append($('<textarea>').addClass('_2LhMgE form-control').val(content).css('min-height', '100px').attr({
		  'rows': '3'
			})));
						   		
		   		
		   	$(X3Qjvs[i]).append($('<input>').attr({'type': 'file','multiple': 'multiple'}).addClass('file-input'));
		   	$(X3Qjvs[i]).append($('<div>').attr({'images': ''}).addClass('selected-images'));
		   
		  
		}
		
		var fileInputs = document.getElementsByClassName('file-input');
					for (let i = 0; i < fileInputs.length; i++) {
						
					  var fileInput = fileInputs[i];
					  fileInput.addEventListener('change', function(event) {
						  imageChange(event);
						});
					}
	}
   
    
    function viewRating(event) {
					var button = $(event.target);
					 // Lấy button gây ra sự kiện
					 
					$('#update-rating').attr('data-order-id-value',button.data('order-id'));
					$('#save-rating').addClass("hide");
					$('#update-rating').removeClass("hide");
					
					var content = $('#content');
					  content.empty();
					 var cardBody = button.closest('.card-body');
				  
				  
				  
				cardBody.find('.x5GTyN').each(function(index) {
					
					
					  var x5GTyNValue = cardBody.find('.x5GTyN').eq(index).text();
					  var vb0b = cardBody.find('.vb0b-P').eq(index).text();
					  var img =cardBody.find('.shopee-image__content--blur')[index].getAttribute('src');
					  
					 
					 
				   		var rating = $('<div>').addClass('rating-modal-handler__container rating-modal-handler__container--last');
				   		
				   		var PinpOp = $('<div>').addClass('PinpOp UYED6+');
				   		PinpOp.append($('<div>').addClass('shopee-image__wrapper tJpxcs')
				   		.append($('<div>').addClass('shopee-image__content')
				   		.append($('<img>').addClass('shopee_img').attr('src', img))
				   		));
				   		var U2w1su =$('<div>').addClass('U2w1su');
				   		U2w1su.append($('<div>').addClass('D7LjRB').attr('data-id', cardBody.find('.x5GTyN').eq(index).attr('data-id')).text(x5GTyNValue));
				   		U2w1su.append($('<div>').addClass('q7YFPv').text(vb0b));
				   		
				   		PinpOp.append(U2w1su);
				   		rating.append(PinpOp);
				   		//get rate 
				   		$.ajax({
				      url: '/rate/getByOrder?orderId='+button.data('order-id')+'&productId='+cardBody.find('.x5GTyN').eq(index).attr('data-id'),
				      type: 'GET',
				      dataType: 'json',
				      
				      success: function(rateResponseDtos) {
						  //rate id
						  U2w1su.attr('data-rate-id', rateResponseDtos.id);
						  //star
				   		var star = $('<div>').css('margin', '20px 0px');
				   		var dmvG7c = ($('<div>').addClass('dmvG7c'));
				   		dmvG7c.append($('<div>').addClass('jcQ0KT').append($('<span>').text("rate")));
				   		
				   		var containStar= $('<div>').css('padding-left', '5px');
				   		var ratingStars = $('<div>').attr('data-rate', 5).addClass('rating-stars');
				   		for(var i=1;i<=5;i++){
						
						if(i <= rateResponseDtos.rating){
							
							ratingStars.append($('<span>').attr({'data-number': i}).addClass('rating-star active').html('&#9733;').on('click', function(event) {
							  // Xử lý khi phần tử được click
							  starChange(event);}))
							
						}else{
							ratingStars.append($('<span>').attr({'data-number': i}).addClass('rating-star gray-color').html('&#9733;').on('click', function(event) {
							  // Xử lý khi phần tử được click
							  starChange(event);}))
							
						}
						
					}
				   		
						containStar.append(ratingStars);
						dmvG7c.append(containStar);
						
				   		star.append(dmvG7c);
				   		rating.append(star);
				   		//jz-Ezz
				   		var jzEzz = $('<div>').addClass('jz-Ezz');
				   		var X3Qjvs = $('<div>').addClass('X3Qjvs').append($('<div>').addClass('CLFTQP'));
						X3Qjvs.append($('<div>').css('position', 'relative').append($('<p>').addClass('_2LhMgE form-control').text(rateResponseDtos.content!=''?rateResponseDtos.content : 'The customer did not leave any comments.')));
				   		
				   		var imageList=$('<div>').addClass('imageList');

						for(var i =0;i<rateResponseDtos.images.length;i++){
							
							  imageList.append($('<img>').attr('src', "../upload/"+rateResponseDtos.images[i].inmageForSave).addClass('shopee-image__content--blur'))
							
						}
					   		
					   	X3Qjvs.append(imageList);
					   	
					   		//response
					if(rateResponseDtos.responses.length!=0){
						var  fwJamt =  $('<div>').addClass('fwJamt');
						var  response;

						for(var i =0;i<rateResponseDtos.responses.length;i++){
							  response=  $('<div>').addClass('response');
							if(rateResponseDtos.responses[i].userResponseDto.roleName=="ADMIN"){
								response.append($('<div>').addClass('CaoTou').text("Puu Shop"));
							}else{
								response.append($('<div>').addClass('CaoTou').text(rateResponseDtos.responses[i].userResponseDto.userName));
							}
							response.append($('<div>').addClass('_2kece8').text(rateResponseDtos.responses[i].content));
							fwJamt.append(response);
						}
						
						
						X3Qjvs.append(fwJamt);
					}
					   
					   	jzEzz.append(X3Qjvs);
					   	rating.append(jzEzz);
					   	content.append(rating);
						  
					  },
	      			  error: function(){}})
				   		
				   		
				 })
			
		document.getElementById("modal").style.display = "block";
		
		    
	}
	
	var csrfToken;
	//var orderId;
    function addToCart(event) {
	        
	         var button = event.target;
			var id = $(button).data('order-id');
			  csrfToken = Cookies.get('XSRF-TOKEN');
			  
			  // Kiểm tra xem token có tồn tại hay không
			  if (csrfToken) {
			    
			    // Gửi Ajax request
			    $.ajax({
			      url: '/cart/addByOrder?orderId=' +id,
			      type: 'POST',
			      contentType: "application/json",
			      //data: JSON.stringify({ orderId: parseInt(id) }),
			      headers: {
			        'X-XSRF-TOKEN': csrfToken
			      },
			      success: function(response,textStatus, jqXHR) {
					
				    var contentType = jqXHR.getResponseHeader("Content-Type");
				    if (contentType.includes("text/html")) {
						
				      // Redirect the user to the login page
				      window.location.href = "/login";
				    } 
				    window.location.href = "/cart";
			      },
			      error: function(xhr, status, error) {
					  alert(status);
				       //alert("The system has errors!");
			      }
			    });
			  } 
    }
//get data for table for first
		var formattedCurrentPrice;
		loadData(0,0,"");
		//get data for table
		function loadData(page,status,key) {
			var orderWrap = ".order-wrap";
		    $.ajax({
		      url: '/order/user/getOrderPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {page : page,status: status,keyWord:key},
		      success: function(orderPageResponseDto) {
		        //clear table
		        $(orderWrap).empty();
				
		        //add record
		        $.each(orderPageResponseDto.orderResponseDtos, function(index, order) {
					
					var hiXKxx = $('<div>').addClass('hiXKxx');
					var card = $('<div>').addClass('card');
					
					//Cart
					//1
					//KrPQEI
					var KrPQEI = $('<div>').addClass('KrPQEI');
					KrPQEI.append($('<div>').addClass('qCUYY8').append($('<div>').addClass('_9Ro5mP').text("Puu Store")));
					
					
					var EQko8g = $('<div>').addClass('EQko8g');
					var KmBIg2 = $('<a>').addClass('KmBIg2');
					KmBIg2.href='#';
					var message;
					if(order.status==1){
						message="Đơn hàng đang đợi xác nhận.";
					}else if(order.status==2){
						message="Đơn hàng đã bị hủy.";
					}else if(order.status==3){
						message="Đơn hàng đã được xác nhận.";
					}else if(order.status==4){
						message="Đơn hàng đã được giao thành công.";
					}
					var nkmfr2 = $('<span>').addClass('nkmfr2');
					//nkmfr2.append($('<i>').addClass('fa fa-truck').attr('aria-hidden', true));
					nkmfr2.append("<i class='fa fa-truck' aria-hidden='true'></i>");
					
					nkmfr2.text(message).click(redirectToDetail);
					KmBIg2.append(nkmfr2);
					EQko8g.append(KmBIg2);
					KrPQEI.append(EQko8g)
					//card add KrPQEI
					card.append(KrPQEI);
					
					//2
					//card-body
					var cardBody = $('<div>').addClass('card-body');
					//add id
					cardBody.append($('<div>').text(order.id).hide().addClass('order-id'));
					//row(product)
					
					$.each(order.orderDetaitsResponseDtos, function(index, orderDetails) {
						var row = $('<div>').addClass('row').click(redirectToDetail);
						//col-md-2
						var subRow = $('<div>').addClass('col-md-2');
						var shopeeImageWrapper = $('<div>').addClass('shopee-image__wrapper');
						var shopeeImagePlaceHolder = $('<div>').addClass('shopee-image__place-holder');
						//var imageElement = $('<img>').addClass("shopee-image__content--blur").attr('src', '../upload/' + orderDetails.imageForSave);
						 shopeeImagePlaceHolder.append($('<img>').addClass("shopee-image__content--blur").attr('src', '../upload/' + orderDetails.imageForSave));
						 shopeeImageWrapper.append(shopeeImagePlaceHolder);
						 subRow.append(shopeeImageWrapper);
						 row.append(subRow);
						 
						 //col-md-8
						var subRow2 = $('<div>').addClass('col-md-8');
						var _7uZf6Q = $('<div>').addClass('_7uZf6Q');
						
						var iJlxsT = $('<div>').append($('<div>').addClass('iJlxsT').append($('<span>').attr({
		            	'data-id': orderDetails.productResponseDto.id

		            }).addClass('x5GTyN').text(orderDetails.productResponseDto.title)));
						_7uZf6Q.append(iJlxsT);
						
						var div = $('<div>');
						if(orderDetails.productAttributeValue.size!=null || orderDetails.productAttributeValue.color!=null){
							if(orderDetails.productAttributeValue.size!=null && orderDetails.productAttributeValue.color!=null){
								div.append( $('<div>').addClass('vb0b-P').text(orderDetails.productAttributeValue.size + ' | ' +orderDetails.productAttributeValue.color));
							}else if(orderDetails.productAttributeValue.size!=null && orderDetails.productAttributeValue.color==null){
								div.append( $('<div>').addClass('vb0b-P').text(orderDetails.productAttributeValue.size));
							}else if(orderDetails.productAttributeValue.size==null && orderDetails.productAttributeValue.color!=null){
								div.append( $('<div>').addClass('vb0b-P').text(orderDetails.productAttributeValue.color));
							}
						}
						 
						 div.append( $('<div>').addClass('_3F1-5M').text('x'+orderDetails.number));
						
						 _7uZf6Q.append(div);
						 
						 subRow2.append(_7uZf6Q);
						 row.append(subRow2);
						 
						 //sub3
						 var subRow3 = $('<div>').addClass('col-md-2');
						 var textCenter = $('<div>').addClass('text-center').css('margin-top', '60px');
						 var rjqzk1 = $('<div>').addClass('rjqzk1');
						 formattedPrice =orderDetails.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
						 if(orderDetails.curentPrice!=0){
							 formattedCurrentPrice =orderDetails.curentPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
							 
							 rjqzk1.append($('<span>').addClass('j2En5+').text(formattedCurrentPrice));
							 rjqzk1.append($('<span>').addClass('-x3Dqh OkfGBc').text(formattedPrice));
						 }else{
							 rjqzk1.append($('<span>').addClass('-x3Dqh OkfGBc').text(formattedPrice));
						 }
						 
						 textCenter.append(rjqzk1);
						 subRow3.append(textCenter);
						 row.append(subRow3);
						 
						 cardBody.append(row);
					});
					
					
					
					 
					 //kvXy0c
					 formattedTotal =order.totalMoney.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
					var kvXy0c = $('<div>').addClass('kvXy0c');
					var div78s2g= $('<div>').addClass('-78s2g').append($('<div>').addClass('_0NMXyN').text('Total: ')).append($('<div>').addClass('DeWpya').text(formattedTotal).click(redirectToDetail));
					kvXy0c.append(div78s2g);
					cardBody.append(kvXy0c);
					
					//AM4Cxf
					var AM4Cxf = $('<div>').addClass('AM4Cxf');
					//var qtUncs = $('<div>').addClass('qtUncs').append($('<span>').addClass('_0NMXyN').text('Không nhận được đánh giá'));
					AM4Cxf.append(  $('<div>').addClass('_8ex9dW').text('7 days return and exchange'));
					
					var EOjXew_4IR9IT = $('<div>').addClass('EOjXew _4IR9IT');
					if(order.status==1){
						var PF0AU = $('<div>').addClass('PF0-AU').append($('<button>').attr({
		            	'data-order-id': order.id,
		            	'data-update-status' : 2,
		                'data-bs-toggle': 'modal',
		                'data-bs-target': '#refuseOrder'
		            }).addClass('stardust-button stardust-button--primary WgYvse cancelOrderBtn').text("Cancel")
		            
		            	.on('click', function(event) {
						  // Xử lý khi phần tử được click
						  cancleOrder(event);
						})
		            );
						EOjXew_4IR9IT.append(PF0AU);
					}else if(order.status==4 || order.status==2){
						var PF0AU = $('<div>').attr({
		            	'data-order-id': order.id
		            }).addClass('PF0-AU').append($('<button>').attr({
		            	'data-order-id': order.id
		            }).addClass('stardust-button stardust-button--primary WgYvse buy-again-btn').text("Buy Again").on('click', function(event) {
						  // Xử lý khi phần tử được click
						  addToCart(event);
						}));
					
						EOjXew_4IR9IT.append(PF0AU);
					}
					
					
					var PgtIur= $('<div>').addClass('PgtIur').append($('<button>').addClass('stardust-button stardust-button--primary WgYvse btn-contact mr-10').text("Contact Seller"));
					EOjXew_4IR9IT.append(PgtIur);
					
					if((order.status==4 || order.status==2) && order.rated == false){
						EOjXew_4IR9IT.append($('<button>').attr({
		            	'data-order-id': order.id
		            }).addClass('stardust-button stardust-button--primary WgYvse rating-btn ').text("Rating").on('click', function(event) {
						  // Xử lý khi phần tử được click
						  openModal(event);
						}));
					}else if((order.status==4 || order.status==2) && order.rated == true){
						EOjXew_4IR9IT.append($('<button>').attr({
		            	'data-order-id': order.id
		            }).addClass('stardust-button stardust-button--primary WgYvse rating-btn ').text("View rating").on('click', function(event) {
						  // Xử lý khi phần tử được click
						  viewRating(event);
						}));
					}
					AM4Cxf.append(EOjXew_4IR9IT);
					cardBody.append(AM4Cxf);
					card.append(cardBody);
					hiXKxx.append(card);
					$(orderWrap).append(hiXKxx);
					
		        });
		         pagination = '';
		         currentPage = orderPageResponseDto.currentPage;
		         totalPages = orderPageResponseDto.totalPages;
		         size = orderPageResponseDto.size;
		        //create pagination 
		      createPagination("#paging",pagination,currentPage,totalPages,status,key );
		      },
		      error: function(){}})}
		       //function to create pagination after call ajax
	     function createPagination(navId,pagination,currentPage,totalPages,status,key ){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +','+status+', \''+key+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +','+status+', \''+key+'\')">' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +','+status+', \''+key+'\')" >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1) +','+status+', \''+key+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
		      ///
		       function openModal(event) {
					var button = $(event.target);
					 // Lấy button gây ra sự kiện
					 
					$('#save-rating').attr('data-order-id-value',button.data('order-id'));
					$('#save-rating').removeClass("hide");
					$('#update-rating').addClass("hide");
					
					
					var content = $('#content');
					  content.empty();
					  var cardBody = button.closest('.card-body'); 
				  
				cardBody.find('.x5GTyN').each(function(index) {
					
					  var x5GTyNValue = cardBody.find('.x5GTyN').eq(index).text();
					  var vb0b = cardBody.find('.vb0b-P').eq(index).text();
					  var img =cardBody.find('.shopee-image__content--blur')[index].getAttribute('src');
					  
					 
					 
				   		var rating = $('<div>').addClass('rating-modal-handler__container rating-modal-handler__container--last');
				   		
				   		var PinpOp = $('<div>').addClass('PinpOp UYED6+');
				   		PinpOp.append($('<div>').addClass('shopee-image__wrapper tJpxcs')
				   		.append($('<div>').addClass('shopee-image__content')
				   		.append($('<img>').addClass('shopee_img').attr('src', img))
				   		));
				   		var U2w1su =$('<div>').addClass('U2w1su').attr('data-rate-id', -1);
				   		U2w1su.append($('<div>').addClass('D7LjRB').attr('data-id', cardBody.find('.x5GTyN').eq(index).attr('data-id')).text(x5GTyNValue));
				   		U2w1su.append($('<div>').addClass('q7YFPv').text(vb0b));
				   		
				   		PinpOp.append(U2w1su);
				   		rating.append(PinpOp);
				   		//star
				   		var star = $('<div>').css('margin', '20px 0px');
				   		var dmvG7c = ($('<div>').addClass('dmvG7c'));
				   		dmvG7c.append($('<div>').addClass('jcQ0KT').append($('<span>').text("Rating")));
				   		
				   		var containStar= $('<div>').css('padding-left', '5px');
				   		var ratingStars = $('<div>').attr('data-rate', 5).addClass('rating-stars');
				   		for(var i=1; i<=5;i++){
							ratingStars.append($('<span>').attr({'data-number': i}).addClass('rating-star active').html('&#9733;').on('click', function(event) {
						  // Xử lý khi phần tử được click
						  starChange(event);
						}))   
						}
						containStar.append(ratingStars);
						dmvG7c.append(containStar);
						dmvG7c.append($('<span>').addClass('_6m3yfK rating-description').text("Terrible").css('color', 'rgb(237, 165, 0)'));
				   		star.append(dmvG7c);
				   		rating.append(star);
				   		//jz-Ezz
				   		var jzEzz = $('<div>').addClass('jz-Ezz');
				   		var X3Qjvs = $('<div>').addClass('X3Qjvs').append($('<div>').addClass('CLFTQP'));
						X3Qjvs.append($('<div>').css('position', 'relative').append($('<textarea>').addClass('_2LhMgE form-control').css('min-height', '100px').attr({
	  'rows': '3',
	  'placeholder': 'Please share what you like about this product with other buyers.'
	})));
				   		
				   		
				   	X3Qjvs.append($('<input>').attr({'type': 'file','multiple': 'multiple'}).addClass('file-input'));
				   	X3Qjvs.append($('<div>').attr({'images': ''}).addClass('selected-images'));
				   	jzEzz.append(X3Qjvs);
				   	rating.append(jzEzz);
				   	content.append(rating);
				 })

				
            
	                document.getElementById("modal").style.display = "block";
	                var fileInputs = document.getElementsByClassName('file-input');
					for (let i = 0; i < fileInputs.length; i++) {
						
					  var fileInput = fileInputs[i];
					  fileInput.addEventListener('change', function(event) {
						  imageChange(event);
						});
					}
	                
	            }
				
				
				
	            function closeModal() {
	                document.getElementById("modal").style.display = "none";
	            }
	      		
				function starChange(event){
					var ratingStars = $(event.target).closest('.rating-stars');
					var ratingDescription = $(event.target).closest('.dmvG7c').find('.rating-description');
					
					var subRatingStar = ratingStars.find('.rating-star').toArray();
					var number = $(event.target).data('number');
					   
                        // Remove active class from all stars
                        for (let index = 0; index < subRatingStar.length; index++) {
                           subRatingStar[index].classList.remove('active');
                           subRatingStar[index].classList.add('gray-star');
                        };
		
                        // Add active class to clicked star and previous stars
                        for (let i = 0; i < number; i++) {
                            subRatingStar[i].classList.add('active');
                        }

                        // Update rating description based on the selected rating
                        switch (number+1) {
                            case 1:
                                ratingDescription.text('Terrible');
                                ratingStars.attr('data-rate', 1);
                                break;
                            case 2:
                                ratingDescription.text('Poor');
                                 ratingStars.attr('data-rate', 2);
                                break;
                            case 3:
                                ratingDescription.text('Fair');
                                 ratingStars.attr('data-rate', 3);
                                break;
                            case 4:
                                ratingDescription.text('Good');
                                 ratingStars.attr('data-rate', 4);
                                break;
                            case 5:
                                ratingDescription.text('Amazing');
                                 ratingStars.attr('data-rate', 5);
                                break;
                            default:
                                ratingDescription.text('');
                                break;
                        }

                    };
           //IMAGE
	  
		// Mảng chứa các file đã chọn
		let selectedFiles = [];
		function imageChange(event){
			var fileInput = event.target;
			
			var selectedImagesContainer = $(fileInput).closest('.X3Qjvs').find('.selected-images');
  			
			// Xóa các ảnh đã hiển thị trước đó
			  selectedImagesContainer.empty();
			
		    // Lấy danh sách các file đã chọn
		    if(fileInput!=null && fileInput.files != undefined){
				const files = fileInput.files;
				selectedFiles=[];
				// Thêm các file mới vào mảng
			    for (let i = 0; i < files.length; i++) {
			        selectedFiles.push(files[i]);
			    }
			}
			
		    // Hiển thị các ảnh đã chọn trên màn hình
		    for (let i = 0; i < selectedFiles.length; i++) {
		        // Tạo khung chứa ảnh
		        const imageContainer = document.createElement('div');
		        imageContainer.className = 'selected-image';
		       

		        // Tạo đối tượng ảnh
		        const image = document.createElement('img');
		        image.src = URL.createObjectURL(selectedFiles[i]);
		       
		       
		        

		        
		     // Thêm ảnh vào khung chứa
		        imageContainer.appendChild(image);
				
		        // Thêm khung chứa ảnh vào khung hiển thị ảnh đã chọn
			        selectedImagesContainer.append(imageContainer);
		        
		      
		    }	        
		};
	  
	      var csrfToken;
	     
		function saveRating() {
			
			
		    var containerRate= document.getElementsByClassName('rating-modal-handler__container');
		    
		    csrfToken = Cookies.get('XSRF-TOKEN');
		    for (let index = 0; index < containerRate.length; index++) {
				var inputs = containerRate[index].querySelector('.file-input').files;
				
				var formData = new FormData();
				for (let i = 0; i < inputs.length; i++) {
		       		formData.append('file', inputs[i]);
		    	}
		    	
		    	if(containerRate[index].querySelector('.U2w1su').getAttribute('data-rate-id')!= -1){
					formData.append('rateId',containerRate[index].querySelector('.U2w1su').getAttribute('data-rate-id'));
				}
                formData.append('productId', $(containerRate[index]).find('.D7LjRB').attr('data-id'));
                formData.append('rating', $(containerRate[index]).find('.rating-stars').attr('data-rate'));  
                formData.append('content', $(containerRate[index]).find('._2LhMgE').val()); 
                formData.append('orderId', $('#save-rating').data('order-id-value')); 
                
                $.ajax({
		        url: '/rate/add',
		        type: 'POST',
		        contentType: false,
		        processData: false,
		        data: formData,
		        dataType: 'json',
		        cache: false,
		         headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
		        success: function(data) {
		        	
		        	
		        	
		        },
		        error: function(jqXHR, textStatus, errorMessage) {
					 
		             	
		        	}
		    	});    
		    	  
		    	
		    	
            };
             document.getElementById("modal").style.display = "none";
				$('#successRating').fadeIn();
		
		    	setTimeout(function() {
		    	  $('#successRating').fadeOut();
		    	}, 2000);
            }
		    
				
	     function cancleOrder(event) {
		      //event.stopPropagation();
		
		      // Hiển thị modal "#refuseOrder"
		      $("#refuseOrder").modal("show");
		    };
	      
	      $('#refuseOrder').on('show.bs.modal', function (e) {
        var button = e.relatedTarget;
       	var orderId = $(button).data('order-id');
	
		$('#refuse-order-modal').data('id', orderId);      
	                
	   })
	//update status
	var csrfToken;
	var buttonText;
	var undo =false;
	function updateStatus(event){
		csrfToken = Cookies.get('XSRF-TOKEN');
		var button = event.target;
		buttonText = button.textContent;
		if(buttonText=="Undo"){
			undo = true;
		}
		var orderId = $(button).data('id');
		var status =$(button).data('update-status');
		$.ajax({
	      url: '/order/updateStatus',
	      type: 'PUT',
	      dataType: 'json',
	      data: {orderId : parseInt(orderId),status: parseInt(status),undo : undo},
	       headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
	      success: function() {
			$('#refuseOrder').modal('hide');
			$('.modal-backdrop').remove();
	        loadData(currentPageForEditAndDelete,0);
	        
	      },
	      error: function(jqXHR, textStatus, errorThrown){
			  var errors = jqXHR.responseJSON;
			  alert(errors);
		  }})
	}
	   	
    
    //event buy again
   
	
    
    
    //search
	$('#search-btn').on('click',function() {
		loadData(0,0,$("#keyword").val());
		
	})
     
     
     