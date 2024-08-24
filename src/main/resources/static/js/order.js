var size;
	//get current page to load page when delete or update a record
	var currentPageForEditAndDelete;
	$('table tbody').on('click', 'tr', function() {
		var firstRow = $(this).closest('tbody').find('tr:first');
		var startIndex = firstRow.children('td:first').text();
		currentPageForEditAndDelete = Math.ceil((startIndex - 1) / size);
	});
		//get data for table for first
		loadData(0,0,false,"",null,null,null);
		//get data for table
		function loadData(page,status,search,key,createAt,startDate,endDate) {
			var table = "#all-order-table tbody";
		
		    $.ajax({
		      url: '/order/getOrderPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {page : page,status: status,search:search,key:key,createAt:createAt,startDate:startDate,endDate:endDate},
		      success: function(data) {
				  if(data==null){
					  return;
				  }
		        //clear table
		        $(table).empty();
				
		        //add record
		        $.each(data.orderResponseDtos, function(index, order) {
		        	var row = $('<tr>');
		            row.append($('<td>').text(data.currentPage * data.size + index+1));
		            row.append($('<td>').text(order.id));
		            row.append($('<td>').text(order.userName));
		            row.append($('<td>').text(order.address));
		            row.append($('<td>').text(order.createAt));
		            var total = order.totalMoney.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
		            row.append($('<td>').text(total));
		            
		            
		            if(order.status==1){
						row.append($('<td>').text("New order"));
						row.append($('<td>').html("<button data-id="+order.id+" onclick='getOrderDetails(event)'  class='btn btn-sm button-details' type='button'>Details</button>"));
						row.append($('<td>').addClass('text-danger').html("<button data-id="+order.id+" data-update-status='2' data-bs-toggle='modal' data-bs-target ='#refuseOrder'  class='btn btn-sm btn-danger' type='button'>Refuse</button> <button onclick='updateStatus(event)' data-id="+order.id+" data-update-status='3' class='btn btn-sm btn-primary' type='button'>Agree</button>"));
		            	
		            	
					}else if(order.status==2){
						row.append($('<td>').text("Cancelled."));
						row.append($('<td>').html("<button data-id="+order.id+"  onclick='getOrderDetails(event)' class='btn btn-sm button-details' type='button'>Details</button>"));
						row.append($('<td>').text("Order has been cancelled."));
					}else if(order.status==3){
						row.append($('<td>').text("Order has been confirm."));
						row.append($('<td>').html("<button data-id="+order.id+" onclick='getOrderDetails(event)' class='btn btn-sm button-details' type='button'>Details</button>"));
						row.append($('<td>').addClass('text-danger').html('<button onclick="updateStatus(event)" data-id='+order.id+' data-update-status="4" class="btn btn-sm btn-success" type="button">Complete</button> <button data-id='+order.id+' data-update-status="2" data-bs-toggle="modal" data-bs-target ="#refuseOrder"  class="btn btn-sm btn-danger" type="button">Cancle</button>'));
		            	
					}else if(order.status==4){
						row.append($('<td>').text("Completed"));
						row.append($('<td>').html("<button data-id="+order.id+" onclick='getOrderDetails(event)' class='btn btn-sm button-details' type='button'>Details</button> "));
						//row.append($('<td>').text("Order has been completed."));
						row.append($('<td>').html("<button onclick='updateStatus(event)' data-id=" + order.id + " data-update-status='3' class='btn btn-sm btn-primary' type='button'>Undo</button> "));
						
					}
					
		            
		            $(table).append(row);
		        });
		         pagination = '';
		         currentPage = data.currentPage;
		         totalPages = data.totalPages;
		         size = data.size;
		        //create pagination 
		       createPagination("#paging",pagination,currentPage,totalPages,status,search,key,createAt,startDate,endDate );
		      },
		      error: function(){}})}
	     
	  
	 
	
	      
	      //function to create pagination after call ajax
	      function createPagination(navId,pagination,currentPage,totalPages, status,search,key,dateValue,startDateValue,endDateValue){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')">' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')" >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1) +',\''+status +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
	 $('#refuseOrder').on('show.bs.modal', function (e) {
        var button = e.relatedTarget;
       	var orderId = $(button).data('id');
		  
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
	        loadData(currentPageForEditAndDelete,0);
	        
	      },
	      error: function(jqXHR, textStatus, errorThrown){
			  var errors = jqXHR.responseJSON;
			  alert(errors);
		  }})
	}
	
	//show order details
	
	function getOrderDetails(event) {
		var button = event.target;
		var orderId = $(button).data('id');
	  // Tạo URL mới với orderId
	  var url = '/orderDetails/get?orderId=' + orderId;
  
	  // Chuyển hướng đến URL mới
	  window.location.href = url;
	};
	
	
	var timeSelect = document.getElementById("time");
	
	
  	var dateWrap = document.getElementById("date-wrap");
  	var startDate = document.getElementById("startDate-wrap");
  	var endDate = document.getElementById("endDate-wrap");

  timeSelect.addEventListener("change", function() {
	 
    if (this.value === "date") {
      dateWrap.classList.remove("hide");
    } else {
      dateWrap.classList.add("hide");
    }
    if (this.value === "dateRange") {
	
     startDate.classList.remove("hide");
     endDate.classList.remove("hide");
      
    } else {
      startDate.classList.add("hide");
      endDate.classList.add("hide");
    }
  });
  

var searchButton = document.getElementById("searchButton");
var  keyInput = document.getElementById("key");
var selectStatus = document.getElementById("selectStatus");

searchButton.addEventListener("click", function() {
	var startDateValue="";
	var endDateValue ="";
	var dateValue ="";
	var key="";
	var search=false;
	var status=0;
	
	 if(keyInput.value !== ""){
		 key=keyInput.value;
		 search=true;
	 }
  if (timeSelect.value !== "choose" && timeSelect.value !== "") {
	  search=true;
	 
    if (timeSelect.value === "date" && document.getElementById("dateInput").value!="" && document.getElementById("dateInput").value!=null) {
      dateValue =document.getElementById("dateInput").value;
      
    } else if(timeSelect.value === "dateRange" && (document.getElementById("startDate").value!="" || document.getElementById("endDate").value!="")) {
		startDateValue =document.getElementById("startDate").value;
		endDateValue =document.getElementById("endDate").value;
		
    }else if(timeSelect.value === "today"){
		var date = new Date(); // Ngày hiện tại
		 dateValue = date.toISOString().substring(0, 10);
		
	}else if(timeSelect.value === "lastDate"){
		var date = new Date();
		var yesterday = new Date(date);
		yesterday.setDate(date.getDate() - 1);
		 dateValue =yesterday.toISOString().substring(0, 10);
		
	}
  }
  //status
	if (selectStatus.value !== "choose" && selectStatus.value !== "") {
	  search=true;
	  switch (selectStatus.value) {
	  case "1":
	    status=1;
	    break;
	  case "2":
	     status=2;
	    break;
	  case "3":
	     status=3;
	    break;
	  case "4":
	    status=4;
	    break;
	}
  }
  
  loadData(0,status,search,key,dateValue,startDateValue,endDateValue);
});


var resetButton = document.getElementById("reset");

// Gán hàm xử lý sự kiện khi nhấp vào nút "reset"
resetButton.addEventListener("click", function() {
 	loadData(0,0,false,"",null,null,null);
 	keyInput.value="";
 	if (timeSelect.value === "date") {
	     
      dateWrap.classList.add("hide");
    }else
    if (timeSelect.value === "dateRange") {
	startDate.classList.add("hide");
      endDate.classList.add("hide");
    }
 	timeSelect.value="choose";
 	selectStatus.value="choose";
});