var currentPageForEditAndDelete;
	$('table tbody').on('click', 'tr', function() {
		var firstRow = $(this).closest('tbody').find('tr:first');
		var startIndex = firstRow.children('td:first').text();
		currentPageForEditAndDelete = Math.ceil((startIndex - 1) / size);
	});
//get data for table for first
		//vì đã hoàn thành đơn hàng nên status là 4
		//get data for table for first
		loadData(0,false,"",null,null,null);
		var totalMoney;
		//get data for table
		function loadData(page,search,key,completedAt,startDate,endDate) {
			var table = "#report-table tbody";
			
		    $.ajax({
		      url: '/report/getOrderPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {page : page,status: status,search:search,key:key,completedAt:completedAt,startDate:startDate,endDate:endDate},
		      success: function(data) {
				  if(data==null){
					  return;
				  }
		        //clear table
		        $(table).empty();
				totalMoney=0;
		        //add record
		        $.each(data.orderResponseDtos, function(index, order) {
					
		        	var row = $('<tr>');
		            row.append($('<td>').text(order.id));
		            row.append($('<td>').text(order.completedAt));
		            row.append($('<td>').text("Payment on delivery"));
		            row.append($('<td>').text(order.userName));
		            var total = order.totalMoney.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
		            row.append($('<td>').text(total));
		            totalMoney+=order.totalMoney;
		            $(table).append(row);
		        });
		        
		        $('#price').text(totalMoney.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }));
		   
		         pagination = '';
		         currentPage = data.currentPage;
		         totalPages = data.totalPages;
		         size = data.size;
		        //create pagination 
		      createPagination("#paging",pagination,currentPage,totalPages,status,search,key,completedAt,startDate,endDate );
		      },
		      error: function(){}})}
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
	  
	  
	  loadData(0,search,key,dateValue,startDateValue,endDateValue);
	});
	      
	      
	       //function to create pagination after call ajax
	      function createPagination(navId,pagination,currentPage,totalPages,search,key,dateValue,startDateValue,endDateValue){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')">' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')" >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1)  +'\',\''+search+'\',\''+key+'\',\''+dateValue+'\',\''+startDateValue+'\',\''+endDateValue+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
	
	
	
	
var resetButton = document.getElementById("reset");

// Gán hàm xử lý sự kiện khi nhấp vào nút "reset"
resetButton.addEventListener("click", function() {
 	loadData(0,false,"",null,null,null);
 	keyInput.value="";
 	
 	if (timeSelect.value === "date") {
	     
      dateWrap.classList.add("hide");
    }else
    if (timeSelect.value === "dateRange") {
	startDate.classList.add("hide");
      endDate.classList.add("hide");
    }
    timeSelect.value="choose";
});




//more information
// Hàm để lấy một màu ngẫu nhiên từ mảng
function getRandomColor() {
  var letters = "0123456789ABCDEF";
  var color = "#";
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

function categoryPie(){
	var xValues=[];
	var yValues=[];
	var barColors=[];
	 $.ajax({
		      url: '/report/soldOfCategory',
		      type: 'GET',
		      dataType: 'json',
		      success: function(data) {
				  for (var i in data) {
					  xValues.push(i);
					  yValues.push(data[i]);
						barColors.push(getRandomColor()); 
						
						new Chart("myChart", {
						  type: "pie",
						  data: {
						    labels: xValues,
						    datasets: [{
						      backgroundColor: barColors,
						      data: yValues
						    }]
						  },
						  options: {
						    title: {
						      display: true,
						      text: "Category Sales"
						    }
						  }
						});
				  }
		      },
		      error: function(){}})
}
