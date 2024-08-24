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
			var table = "#feedback-table tbody";
		    $.ajax({
		      url: '/feedback/getFeedbackPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {page : page,  keyword : keyWord},
		      success: function(data) {
		        //clear table
		        $(table).empty();
		
		        //add record
		      $.each(data.feedbackResponseDtos, function(index, feedback) {
				    var row = $('<tr>');
				    row.append($('<td>').text(data.currentPage * data.size + index + 1));
				    row.append($('<td>').attr('fullname', 'fullname').text(feedback.fullName));
				    row.append($('<td>').attr('email', 'email').text(feedback.email));
				    row.append($('<td>').attr('createAt', 'createAt').text(feedback.createAt));
				
				    var actionsColumn = $('<td>');
				
				    var viewButton = $('<button>').attr({
				        'data-id': feedback.id,
				        'data-bs-toggle': 'modal',
				        'data-bs-target': '#viewFeedback'
				    }).addClass('btn btn-primary btn-sm').html('<i class="bi bi-eye-fill"></i> View');
				
				    var space = $('<span>').html('&nbsp;');
				
				   // var deleteButton = $('<button>').attr({
				   //     'data-id': feedback.id,
				   //     'data-bs-toggle': 'modal',
				   //     'data-bs-target': '#deleteFeedback'
				   // }).addClass('btn btn-danger btn-sm').html('<i class="bi bi-trash-fill"></i> Delete');
				
				    actionsColumn.append(viewButton);
				    actionsColumn.append(space);
				    //actionsColumn.append(deleteButton);
				
				    row.append(actionsColumn);
				    $(table).append(row);
				});

		         pagination = '';
		         currentPage = data.currentPage;
		         totalPages = data.totalPages;
		         size = data.size;
		        //create pagination 
		       createPagination("#paging",pagination,currentPage,totalPages );
		      },
		      error: function(){}})}
	       //DELETE feedback
		$('#deleteFeedback').on('show.bs.modal', function (e) {
	                var deleteButton = e.relatedTarget;
	                var feedbackId = $(deleteButton).data('id');
	                $('#feedbackIdDeleteModal').val(feedbackId);
	                
	   })
	   //delete user when click button with id 'delete-user' in modal
	   $(document).on("click","#delete-feedback",function(e) {
		   var feedbackId = document.getElementById("feedbackIdDeleteModal").value;
		
		    $.ajax({
		      url: '/feedback/delete?feedbackId='+feedbackId,
		      type: 'DELETE',
		      contentType: 'json',
		      success: function(data) {
		        // close modal
		        $('#deleteFeedback').modal('hide');
		        //notice that the deletion was successful
		        alert(data);
		        //reload pagination
		        loadData(currentPageForEditAndDelete);
		      },
		      error: function(jqXHR, textStatus, errorThrown){
		    	  if (jqXHR.status === 400) {
		    	        var error = jqXHR.responseJSON || jqXHR.responseText;
		    	        $('#deleteFeedback').modal('hide');
		    	        alert(error)
		    	        loadData(currentPageForEditAndDelete);
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
		
		//View feedback
		$('#viewFeedback').on('show.bs.modal', function (e) {
			    var viewButton = e.relatedTarget;
			    var feedbackId = $(viewButton).data('id');
			    $('#feedbackIdViewModal').val(feedbackId);
			   
			    // Gửi yêu cầu AJAX để lấy thông tin chi tiết của feedback theo feedbackId
			    $.ajax({
			        url: '/feedback/viewDetail?feedbackId=' + feedbackId,
			        type: 'GET',
			        success: function(feedback) {
			            // Đặt giá trị cho các phần tử trong modal
			            $('#fullName').text(feedback.fullName);
			            $('#email').text(feedback.email);
			            $('#phone').text(feedback.phone);
			            $('#subjectName').text(feedback.subjectName);
			            $('#note').text(feedback.note);
			        },
			        error: function() {
			            // Xử lý lỗi khi không thể lấy thông tin chi tiết của feedback
			        }
			    });
			});

	   
	       
		 //function to create pagination after call ajax
	      function createPagination(navId,pagination,currentPage,totalPages ){
	    	  $(navId).empty();
	          
	          pagination += '<ul class="pagination justify-content-end">';
	          pagination += '<li ' + (currentPage > 0 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage > 0 ? 'loadData(' + (currentPage - 1) +')' : 'return false;') + '" aria-label="Previous"><span aria-hidden="true">&laquo;</span><span class="sr-only"></span></a></li>';

	              var startPage = currentPage > 2 ? currentPage - 2 : 0;
	              var endPage = currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1;

	              for (var i = startPage; i <= endPage; i++) {
	                  pagination += '<li ' + (currentPage === i ? 'class="page-item active"' : '') + '><a class="page-link" onclick="loadData(' + i +')">' + (i + 1) + '</a></li>';
	              }
	             //${page.totalPages > 5 && page.number < page.totalPages - 3}
	              if ((totalPages > 5) &&  (currentPage < totalPages-3)) {
	                  pagination += '<li class="page-item disabled"><a class="page-link" >...</a></li>';
	              }
	             
	              if ((totalPages > 1) && (totalPages-1 != currentPage) && (totalPages-2 != currentPage) && (currentPage<totalPages-3)) {
	                  pagination += '<li><a class="page-link" onclick="loadData(' + (totalPages - 1) +')" >' + totalPages + '</a></li>';
	              }

	          pagination += '<li ' + (currentPage < totalPages - 1 ? '' : 'class="page-item disabled"') + '><a class="page-link" onclick="' + (currentPage < totalPages - 1 ? 'loadData(' + (currentPage + 1) +')' : 'return false;') + '" aria-label="Next"><span aria-hidden="true">&raquo;</span><span class="sr-only"></span></a></li>';
	          pagination += '</ul>';
	           
	           $(navId).append(pagination);
	      }
	      
	      
	    var searchButton = document.getElementById("feedback-search");
		var  keyInput = document.getElementById("key-word");
		var selectStatus = document.getElementById("selectStatus");
		
		searchButton.addEventListener("click", function() {
			var key=null;
			
			 if(keyInput.value !== ""){
				 key = keyInput.value;
				 loadData(0,key);
			 }else{
				 loadData(0,"");
			 }
		});
		
		