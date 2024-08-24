	var pagination ;
	var currentPage;
	var totalPages;
	var attributeValueName; 
	var attributeName;
	var size;
	var csrfToken;
	//click on the input box, the error line will disappear
	const inputList = ['colorAttributeValueName','sizeAttributeValueName', 'attributeValueNameModal'];
	
	inputList.forEach(function(inputName) {
	  const input = document.getElementById(inputName);
	  const error = document.getElementById(inputName + '-error');
	  input.addEventListener("focus", function() {
		 error.textContent = '';
	  });
	});
	//get current page to load page when delete or update a record
	var currentPageForEditAndDelete;
	$('table tbody').on('click', 'tr', function() {
		var firstRow = $(this).closest('tbody').find('tr:first');
		var startIndex = firstRow.children('td:first').text();
		currentPageForEditAndDelete = Math.ceil((startIndex - 1) / size);
	});
	
	//add attribute value
	
	function addAttribute(event) {
	if(event.target.id == 'addColor'){
		attributeValueName = document.getElementById("colorAttributeValueName").value;
		attributeName = 'color';
	}else if(event.target.id == 'addSize'){
		attributeValueName = document.getElementById("sizeAttributeValueName").value;
		attributeName = 'size';
	}
		 csrfToken = Cookies.get('XSRF-TOKEN');

    $.ajax({
      url: '/attributeValue/add',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ attributeValueName: attributeValueName, attributeName: attributeName }),
      headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
      success: function(data) {
        //clear input attribute name
        document.getElementById("colorAttributeValueName").value = "";
        document.getElementById("sizeAttributeValueName").value = "";
        alert(data)
        //load data
        loadData(0,attributeName,"");
        
        

      },
      error: function(jqXHR, textStatus, errorThrown){
    	  if (jqXHR.status === 400) {
    	        var errors = jqXHR.responseJSON;
    	        if (errors.hasOwnProperty("bindingErrors")) {
    	            var bindingErrors = errors["bindingErrors"];
    	            for (var i = 0; i < bindingErrors.length; i++) {
    	                var error = bindingErrors[i];
    	                //error from binding
    	                $("#"+attributeName+"AttributeValueName-error").text(error.defaultMessage);
    	            }
    	        }
    	        if (Object.keys(errors).length >= 1) {
    	            //other error
    	            for (var key in errors) {
    	                if (key !== "bindingErrors") {
    	                    var error = errors[key];
    	                    $("#"+attributeName+"AttributeValueName-error").text(error);
    	                }
    	            }
    	        }
    	    } else {
    	        // xử lý lỗi khác
    	        console.log(textStatus);
    	        console.log(errorThrown);
    	    }
      }})}	
	//DELETE ATTIBUTE VALUE
	//set attribute value id for modal
	$('#deleteAttributeValue').on('show.bs.modal', function (e) {
                var deleteButton = e.relatedTarget;
                var attributeId = $(deleteButton).data('id');
                var attributeName = $(deleteButton).data('attribute');
                $('#attributeValueIdDeleteModal').val(attributeId);
                $('#attributeNameDeleteModal').val(attributeName);
                
   })
   //delete addtribute value when click button with id 'delete-attribute-value' in modal
   $(document).on("click","#delete-attribute-value",function(e) {
	   var attributeValueId = document.getElementById("attributeValueIdDeleteModal").value;
	   var attributeName = document.getElementById("attributeNameDeleteModal").value;
		csrfToken = Cookies.get('XSRF-TOKEN');
	    $.ajax({
	      url: '/attributeValue/delete?attributeValueId='+attributeValueId,
	      type: 'DELETE',
	      contentType: 'json',
	       headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
	      success: function(data) {
	        // close modal
	        $('#deleteAttributeValue').modal('hide');
	        //notice that the deletion was successful
	        alert(data);
	        //reload pagination
	        loadData(currentPageForEditAndDelete,attributeName,"");
	      },
	      error: function(jqXHR, textStatus, errorThrown){
	    	  if (jqXHR.status === 400) {
	    	        var error = jqXHR.responseJSON || jqXHR.responseText;
	    	        $('#deleteAttributeValue').modal('hide');
	    	        alert(error)
	    	        loadData(currentPageForEditAndDelete,attributeName,"");
	    	    } else {
	    	        // xử lý lỗi khác
	    	        console.log(textStatus);
	    	        console.log(errorThrown);
	    	    }
	      }})
        });
	//click close edit modal , error set empty 
	$(document).on('click', '.close-edit', function() { 
		document.getElementById("attributeValueNameModal-error").textContent = '';
	});
	//EDIT ATTIBUTE VALUE
	//set attribute value id and name for modal
	var editButton;
	var attributeValueName;
	var attributeValueId;
	var attributeName;
	
	$('#editAttributeValue').on('show.bs.modal', function (e) {
				 editButton = e.relatedTarget;
                 attributeValueName = $(editButton).closest('tr').find('td[name="name"]').text();
                 attributeValueId = $(editButton).data('id');
                 attributeName = $(editButton).data('attribute-name');
                $('#attributeValueNameModal').val(attributeValueName);
                $('#attributeValueIdModal').val(attributeValueId);
                $('#attributeNameModal').val(attributeName);
                
   })
   
	//edit addtribute value when click button with id 'edit-attribute-value' in modal
   $(document).on("click","#edit-attribute-value",function(e) {
	   attributeValueId = document.getElementById("attributeValueIdModal").value;
	   attributeValueName =  document.getElementById("attributeValueNameModal").value;
	   attributeName =  document.getElementById("attributeNameModal").value;
		csrfToken = Cookies.get('XSRF-TOKEN');
	    $.ajax({
	      url: '/attributeValue/update?attributeValueId='+attributeValueId,
	      type: 'PUT',
	      contentType: 'application/json',
	      data: JSON.stringify({ attributeValueName: attributeValueName, attributeName: attributeName }),
	       headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
	      success: function(data) {
	        // close modal
	        $('#editAttributeValue').modal('hide');
	        //notice that the deletion was successful
	        alert(data);
	        //reload pagination
	        loadData(currentPageForEditAndDelete,attributeName,"");
	      },
	      error: function(jqXHR, textStatus, errorThrown){
	    	  if (jqXHR.status === 400) {
	    	        var errors = jqXHR.responseJSON;
	    	        if (errors.hasOwnProperty("bindingErrors")) {
	    	            var bindingErrors = errors["bindingErrors"];
	    	            for (var i = 0; i < bindingErrors.length; i++) {
	    	                var error = bindingErrors[i];
	    	                //display error
	    	                $("#attributeValueNameModal-error").text(error.defaultMessage);
	    	            }
	    	        }
	    	        if (Object.keys(errors).length >= 1) {
	    	            //other error
	    	            for (var key in errors) {
	    	                if (key !== "bindingErrors") {
	    	                    var error = errors[key];
	    	                    // đặt thông tin lỗi vào thẻ tương ứng
	    	                    $("#attributeValueNameModal-error").text(error);
	    	                }
	    	            }
	    	        }
	    	    } else {
	    	        // xử lý lỗi khác
	    	        console.log(textStatus);
	    	        console.log(errorThrown);
	    	    }
	      }})
        });
	//get data for table
	loadData(0,"size","");
	function loadData(page,attributeName,key) {
		var table = "#"+attributeName+"-table tbody";
	    $.ajax({
	      url: '/attributeValue/getAttributePage',
	      type: 'GET',
	      dataType: 'json',
	      data: {page : page,attributeName : attributeName,key:key},
	      success: function(data) {
			  //clear table
	       	 $(table).empty();
			  if(data!=null){
				   //add record
			        $.each(data.attributeValueResponseDto, function(index, attributeValue) {
			        	var row = $('<tr>');
			            row.append($('<td>').text(data.currentPage * data.size + index+1));
			            row.append($('<td>').attr('name','name').text(attributeValue.name));
			            
			            row.append($('<td>').attr({
			            	'data-id': attributeValue.id,
			            	'data-attribute-name': attributeName,
			                'data-bs-toggle': 'modal',
			                'data-bs-target': '#editAttributeValue'
			            }).addClass('text-primary').html('<i class="fas fa-pencil-alt"></i>Edit'));
			            
			            row.append($('<td>').attr({
			            	'data-id': attributeValue.id,
			            	'data-attribute': attributeName,
			                'data-bs-toggle': 'modal',
			                'data-bs-target': '#deleteAttributeValue'
			            }).addClass('text-danger').html('<i class="fas fa-trash"></i>Delete'));
			            $(table).append(row);
			        });
			         pagination = '';
			         currentPage = data.currentPage;
			         totalPages = data.totalPages;
			         size = data.size;
			        //create pagination 
			       createPagination("#"+attributeName+"-paging",attributeName,pagination,currentPage,totalPages ,key);
			  }
	        
	
	       
	      },
	      error: function(){}})}
      
      
      //function to create pagination after call ajax
      function createPagination(navId,attributeName,pagination,currentPage,totalPages,key ){
    	  $(navId).empty();
          
          pagination += '<ul class="pagination">';
          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +',\''+ attributeName+ '\',\''+key+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

         
              var startPage = currentPage > 2 ? currentPage - 2 : 0;
              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

              for (var i = startPage; i <= endPage; i++) {
                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +',\''+ attributeName+ '\',\''+key+'\')">' + (i + 1) + '</a></li>';
              }
             //${page.totalPages > 5 && page.number < page.totalPages - 3}
              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
              }
             
              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +',\''+ attributeName+ '\',\''+key+'\')" >' + totalPages + '</a></li>';
              }

          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1) +',\''+ attributeName+ '\',\''+key+'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
          pagination += '</ul>';
           
           $(navId).append(pagination);
      }
      //search for size
    var searchSize = document.getElementById("size-search");
	var  keySize = document.getElementById("size-key-word");
	
		
		searchSize.addEventListener("click", function() {
			var key=null;
			 if(keySize.value !== ""){
				 key=keySize.value;
				 loadData(0,"size",key);
			 }
		});
		
		var resetButton = document.getElementById("reset-size");

		// Gán hàm xử lý sự kiện khi nhấp vào nút "reset"
		resetButton.addEventListener("click", function() {
		 	loadData(0,"size","");
		 	keySize.value="";
		 	
		});
		
		 //search for color
		var searchColor = document.getElementById("color-search");
		var  keyColor = document.getElementById("color-key-word");
	
		
		searchColor.addEventListener("click", function() {
			var key="";
			 if(keyColor.value !== ""){
				 key=keyColor.value;
				 loadData(0,"color",key);
			 }
		});
		
		var resetButton = document.getElementById("reset-color");

		// Gán hàm xử lý sự kiện khi nhấp vào nút "reset"
		resetButton.addEventListener("click", function() {
		 	loadData(0,"color","");
		 	keyColor.value="";
		 	
		});
	
	