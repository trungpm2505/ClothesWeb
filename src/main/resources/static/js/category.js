//click on the input box, the error line will disappear
		const inputList = ['categoryNameInput','categoryNameEditModal'];
		
		inputList.forEach(function(inputName) {
		  const input = document.getElementById(inputName);
		  const error = document.getElementById(inputName + '-error');
		  input.addEventListener("focus", function() {
			 error.textContent = '';
		  });
		});
	
		var size;
		//get current page to load page when delete or update a record
		var currentPageForEditAndDelete;
		$('table tbody').on('click', 'tr', function() {
			var firstRow = $(this).closest('tbody').find('tr:first');
			var startIndex = firstRow.children('td:first').text();
			currentPageForEditAndDelete = Math.ceil((startIndex - 1) / size);
		});
		//get data for table for first
		loadData(0,"");
		//get data for table
		function loadData(page,keyWord) {
			var table = "#category-table tbody";
		    $.ajax({
		      url: '/category/getCategoryPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {page : page,keyword:keyWord},
		      success: function(data) {
		        //clear table
		        $(table).empty();
		
		        //add record
		        $.each(data.categoryResponseDtos, function(index, category) {
		        	var row = $('<tr>');
		            row.append($('<td>').text(data.currentPage * data.size + index+1));
		            row.append($('<td>').attr('name','name').text(category.name));
		            
		            row.append($('<td>').attr({
		            	'data-id': category.id,
		                'data-bs-toggle': 'modal',
		                'data-bs-target': '#editCategory'
		            }).addClass('text-primary').html('<i class="fas fa-pencil-alt"></i>Edit'));
		            
		            row.append($('<td>').attr({
		            	'data-id': category.id,
		                'data-bs-toggle': 'modal',
		                'data-bs-target': '#deleteCategory'
		            }).addClass('text-danger').html('<i class="fas fa-trash"></i>Delete'));
		            $(table).append(row);
		        });
		         pagination = '';
		         currentPage = data.currentPage;
		         totalPages = data.totalPages;
		         size = data.size;
		        //create pagination 
		       createPagination("#paging",pagination,currentPage,totalPages,keyWord );
		      },
		      error: function(){}})}
	      
	      
	      //function to create pagination after call ajax
	      function createPagination(navId,pagination,currentPage,totalPages,keyWord ){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +',\''+keyWord+'\')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	         
	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +',\''+keyWord+'\')">' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +',\''+keyWord+'\')" >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1)+',\''+keyWord +'\')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
	      
	    //add category
	   // var categoryName;
	   var csrfToken;
	  	function addCategory() {
	  		var categoryName = document.getElementById("categoryNameInput").value;
	  		csrfToken = Cookies.get('XSRF-TOKEN');
	      $.ajax({
	        url: '/category/add',
	        type: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify({categoryName: categoryName}),
	         headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
	        success: function(data) {
	          //clear input category name
	          document.getElementById("categoryNameInput").value = "";
	          alert(data)
	          //load data
	          loadData(0,"");
	          
	          

	        },
	        error: function(jqXHR, textStatus, errorThrown){
	      	  if (jqXHR.status === 400) {
	      	        var errors = jqXHR.responseJSON;
	      	        if (errors.hasOwnProperty("bindingErrors")) {
	      	            var bindingErrors = errors["bindingErrors"];
	      	            for (var i = 0; i < bindingErrors.length; i++) {
	      	                var error = bindingErrors[i];
	      	                //error from binding
	      	                $("#categoryNameInput-error").text(error.defaultMessage);
	      	            }
	      	        }
	      	        if (Object.keys(errors).length >= 1) {
	      	            //other error
	      	            for (var key in errors) {
	      	                if (key !== "bindingErrors") {
	      	                    var error = errors[key];
	      	                    $("#categoryNameInput-error").text(error);
	      	                }
	      	            }
	      	        }
	      	    } else {
	      	        // xử lý lỗi khác
	      	       alert("erorr");
	      	    }
	        }})}	
	  	
	  //DELETE ATTIBUTE VALUE
		//set category id for modal
		$('#deleteCategory').on('show.bs.modal', function (e) {
	                var deleteButton = e.relatedTarget;
	                var categoryId = $(deleteButton).data('id');
	                $('#categoryIdDeleteModal').val(categoryId);	               
	                
	   })
	   //delete addtribute value when click button with id 'delete-attribute-value' in modal
	   $(document).on("click","#delete-category-value",function(e) {
		   var categoryId = document.getElementById("categoryIdDeleteModal").value;
			csrfToken = Cookies.get('XSRF-TOKEN');
		    $.ajax({
		      url: '/category/delete?categoryId='+categoryId,
		      type: 'DELETE',
		      contentType: 'json',
		       headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
		      success: function(data) {
		        // close modal
		        $('#deleteCategory').modal('hide');
		        //notice that the deletion was successful
		        alert(data);
		        //reload pagination
		        loadData(currentPageForEditAndDelete,"");
		      },
		      error: function(jqXHR, textStatus, errorThrown){
		    	  if (jqXHR.status === 400) {
		    	        var error = jqXHR.responseJSON || jqXHR.responseText;
		    	        $('#deleteCategory').modal('hide');
		    	        alert(error)
		    	        loadData(currentPageForEditAndDelete,"");
		    	    } else {
		    	        // xử lý lỗi khác
		    	        console.log(textStatus);
		    	        console.log(errorThrown);
		    	    }
		      }})
	        });
	  
		//click close edit modal , error set empty 
		$(document).on('click', '.close-edit', function() { 
			document.getElementById("categoryNameEditModal-error").textContent = '';
		});
		//EDIT ATTIBUTE VALUE
		//set attribute value id and name for modal
		
		$('#editCategory').on('show.bs.modal', function (e) {
					var editButton = e.relatedTarget;
	               var categoryName = $(editButton).closest('tr').find('td[name="name"]').text();
	               var categoryId = $(editButton).data('id');
	                $('#categoryNameEditModal').val(categoryName);
	                $('#attributeValueIdModal').val(categoryId);
	                
	   })
	   
		//edit addtribute value when click button with id 'edit-attribute-value' in modal
	   $(document).on("click","#edit-category-value",function(e) {
		   categoryId = document.getElementById("attributeValueIdModal").value;
		   categoryName =  document.getElementById("categoryNameEditModal").value;
		  csrfToken = Cookies.get('XSRF-TOKEN');
		
		    $.ajax({
		      url: '/category/update?categoryId='+categoryId,
		      type: 'PUT',
		      contentType: 'application/json',
		      data: JSON.stringify({ categoryName: categoryName}),
		       headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
		      success: function(data) {
		        // close modal
		        $('#editCategory').modal('hide');
		        //notice that the deletion was successful
		        alert(data);
		        //reload pagination
		        loadData(currentPageForEditAndDelete,"");
		      },
		      error: function(jqXHR, textStatus, errorThrown){
		    	  if (jqXHR.status === 400) {
		    	        var errors = jqXHR.responseJSON;
		    	        if (errors.hasOwnProperty("bindingErrors")) {
		    	            var bindingErrors = errors["bindingErrors"];
		    	            for (var i = 0; i < bindingErrors.length; i++) {
		    	                var error = bindingErrors[i];
		    	                //display error
		    	                $("#categoryNameEditModal-error").text(error.defaultMessage);
		    	            }
		    	        }
		    	        if (Object.keys(errors).length >= 1) {
		    	            //other error
		    	            for (var key in errors) {
		    	                if (key !== "bindingErrors") {
		    	                    var error = errors[key];
		    	                    // đặt thông tin lỗi vào thẻ tương ứng
		    	                    $("#categoryNameEditModal-error").text(error);
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
	        
     	var searchButton = document.getElementById("category-search");
		var  keyInput = document.getElementById("key-word");
		var selectStatus = document.getElementById("selectStatus");
		
		searchButton.addEventListener("click", function() {
			var key="";
			 if(keyInput.value !== ""){
				 key=keyInput.value;
				 loadData(0,key);
			 }
		});
		
		var resetButton = document.getElementById("reset");

		// Gán hàm xử lý sự kiện khi nhấp vào nút "reset"
		resetButton.addEventListener("click", function() {
		 	loadData(0,"");
		 	keyInput.value="";
		 	
		});
	