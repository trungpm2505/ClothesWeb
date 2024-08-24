var size;
	//get current page to load page when delete or update a record
	var currentPageForEditAndDelete;
	$('table tbody').on('click', 'tr', function() {
		var firstRow = $(this).closest('tbody').find('tr:first');
		var startIndex = firstRow.children('td:first').text();
		currentPageForEditAndDelete = Math.ceil((startIndex - 1) / size);
	});
//get data for table for first
		var formattedCurrentPrice;
		loadData(0,1,"");
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
						message="Đơn hàng đã đucợ xác nhận.";
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
						var iJlxsT = $('<div>').append($('<div>').addClass('iJlxsT').append($('<span>').addClass('x5GTyN').text(orderDetails.productResponseDto.title)));
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
					
					
					var PgtIur= $('<div>').addClass('PgtIur').append($('<button>').addClass('stardust-button stardust-button--primary WgYvse btn-contact').text("Contact Seller"));
					EOjXew_4IR9IT.append(PgtIur);
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
	      
	     function cancleOrder(event) {
		      
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
	        loadData(currentPageForEditAndDelete,2);
	        
	      },
	      error: function(jqXHR, textStatus, errorThrown){
			  var errors = jqXHR.responseJSON;
			  alert(errors);
		  }})
	}
	   	/*var  orderId;
	    function cancel(event) {
        event.stopPropagation();
        //event.preventDefault();
       
			 orderId= document.getElementById("order-id").value;
			 
			  csrfToken = Cookies.get('XSRF-TOKEN');
			  
			  // Kiểm tra xem token có tồn tại hay không
			  if (csrfToken) {
			    // Tạo đối tượng dữ liệu để gửi qua Ajax
			   
			    // Gửi Ajax request
			    $.ajax({
			      url: '/order/updateStatus',
			      type: 'PUT',
			      dataType: 'json',
			      data: {orderId : parseInt(orderId),status: parseInt(2)},
			       headers: {
					    'X-XSRF-TOKEN': csrfToken
					  },
			      success: function() {
					$('#cancelOrder').modal('hide');
					$('.modal-backdrop').remove();
			        loadData(0,0);
			        
			      },
			      error: function(jqXHR, textStatus, errorThrown){
					  var errors = jqXHR.responseJSON;
					  alert(errors);
				  }})
			  } 
    }*/
    
    //event buy again
   
	var csrfToken;
	//var orderId;
    function addToCartInSingleProduct(event) {
	        
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
    
    var thumbnailOrder;
  	var id;
  	function redirectToDetail(event) {
		  thumbnailOrder = event.currentTarget.closest('.card');
		 
		  id = thumbnailOrder.querySelector('.order-id').textContent;
		 
        window.location.href = '/orderDetails/get?orderId='+ id ;
    }

        //search
	$('#search-btn').on('click',function() {
		loadData(0,0,$("#keyword").val());
		
	})
     
     