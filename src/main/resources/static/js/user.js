

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
		
			function loadRole() {
		    const roleSelect = document.getElementById('role');
		    const roleName = document.getElementById('roleNameEditModal').value;
		   
		        $.ajax({
		            url: "/user/getAllRole",
		            type: "GET",
		            dataType: 'json',
		            success: function(roles) {
		                if (roles.length != 0) {
		                    roleSelect.innerHTML = ""; // Xóa các lựa chọn cũ
		
		                    roles.forEach(function(role) {
		                        const optionElement = document.createElement('option');
		                        optionElement.value = role.id;
		                        optionElement.textContent = role.roleName;
		                        
		                         if (roleName === 'ADMIN') {
		                            optionElement.selected = true; // Chọn tự động nếu role.roleName là "ADMIN"
		                        }
		                        
		                        roleSelect.appendChild(optionElement);
		                    });
		
		                    roleSelect.setAttribute('data-loaded', 'true');
		                } else {
		                    // Xử lý khi danh sách có dữ liệu
		                }
		            },
		            error: function(jqXHR, textStatus, errorMessage) {
		                var error = jqXHR.responseJSON || jqXHR.responseText;
		                alert(error);
		                alert("not ok");
		            }
		        });
		    }
		
		//get data for table
		function loadData(page,keyWord) {
			var table = "#user-table tbody";
		    $.ajax({
		      url: '/user/getUserPage',
		      type: 'GET',
		      dataType: 'json',
		      data: {page : page, keyword : keyWord},
		      success: function(data) {
		        //clear table
		        $(table).empty();
		
		        //add record
		        $.each(data.userResponseDtos, function(index, user) {
		        	var row = $('<tr>');
		            row.append($('<td>').text(data.currentPage * data.size + index+1));
		            row.append($('<td>').attr('fullname','fullname').text(user.fullName));
		            row.append($('<td>').attr('email','email').text(user.email));
		            row.append($('<td>').attr('phone','phone').text(user.phone));
		            row.append($('<td>').attr('address','address').text(user.address));
		            row.append($('<td>').attr('roleName','roleName').text(user.roleName));
		            
		            row.append($('<td>').attr({
		            	'data-id': user.id,
		                'data-bs-toggle': 'modal',
		                'data-bs-target': '#editUser'
		            }).addClass('text-primary').html('<i class="fas fa-pencil-alt"></i></i>Set Role'));
		            
		            //row.append($('<td>').attr({
		            //	'data-id': user.id,
		             //   'data-bs-toggle': 'modal',
		            //    'data-bs-target': '#deleteUser'
		           // }).addClass('text-danger').html('<i class="fas fa-trash"></i>Delete')); 
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
	      
	       //DELETE user
		//set user id for modal
		$('#deleteUser').on('show.bs.modal', function (e) {
	                var deleteButton = e.relatedTarget;
	                var userId = $(deleteButton).data('id');
	                $('#userIdDeleteModal').val(userId);
	                
	   })
	   //delete user when click button with id 'delete-user' in modal
	   $(document).on("click","#delete-user",function(e) {
		   var userId = document.getElementById("userIdDeleteModal").value;
		
		    $.ajax({
		      url: '/user/delete?userId='+userId,
		      type: 'DELETE',
		      contentType: 'json',
		      success: function(data) {
		        // close modal
		        $('#deleteUser').modal('hide');
		        //notice that the deletion was successful
		        alert(data);
		        //reload pagination
		        loadData(currentPageForEditAndDelete);
		      },
		      error: function(jqXHR, textStatus, errorThrown){
		    	  if (jqXHR.status === 400) {
		    	        var error = jqXHR.responseJSON || jqXHR.responseText;
		    	        $('#deleteUser').modal('hide');
		    	        alert(error)
		    	        loadData(currentPageForEditAndDelete);
		    	    } else {
		    	        // xử lý lỗi khác
		    	        console.log(textStatus);
		    	        console.log(errorThrown);
		    	    }
		      }})
	        });
	        
	        $('#editUser').on('show.bs.modal', function (e) {
					var editButton = e.relatedTarget;
	               var roleName = $(editButton).closest('tr').find('td[roleName="roleName"]').text();
	               var userId = $(editButton).data('id');
	                $('#roleNameEditModal').val(roleName);
	                $('#userIdEditModal').val(userId);
	                 loadRole();
	                
	   })
	        //edit user value when click button with id 'edit-attribute-value' in modal
	   $(document).on("click","#edit-role",function(e) {
		   userId = document.getElementById("userIdEditModal").value;
		   roleId =  document.getElementById("role").value;
		var csrfToken = Cookies.get('XSRF-TOKEN');
		    $.ajax({
		      url: '/user/update?userId=' + encodeURIComponent(userId) + '&roleId=' + encodeURIComponent(roleId),
		      type: 'PUT',
		      contentType: 'application/json',
		      data: ({userId:parseInt(userId), roleId: parseInt(roleId)}),
		      headers: {
			    'X-XSRF-TOKEN': csrfToken
			  },
		      success: function(data) {
		        // close modal
		        $('#editUser').modal('hide');
		        //notice that the deletion was successful
		        alert(data);
		        //reload pagination
		        loadData(currentPageForEditAndDelete);
		      },
		      error: function(jqXHR, textStatus, errorThrown){
		    	  if (jqXHR.status === 400) {
		    	        var errors = jqXHR.responseJSON;
		    	        if (errors.hasOwnProperty("bindingErrors")) {
		    	            var bindingErrors = errors["bindingErrors"];
		    	            for (var i = 0; i < bindingErrors.length; i++) {
		    	                var error = bindingErrors[i];
		    	                
		    	                //display error
		    	                //$("#categoryNameEditModal-error").text(error.defaultMessage);
		    	            }
		    	        }
		    	        if (Object.keys(errors).length >= 1) {
		    	            //other error
		    	            for (var key in errors) {
		    	                if (key !== "bindingErrors") {
		    	                    var error = errors[key];
		    	                    // đặt thông tin lỗi vào thẻ tương ứng
		    	                    //$("#categoryNameEditModal-error").text(error);
		    	                }
		    	            }
		    	        }
		    	        alert(error);
		    	    } else {
		    	        // xử lý lỗi khác
		    	        console.log(textStatus);
		    	        console.log(errorThrown);
		    	    }
		      }})
	        });
	        
	        
	    var searchButton = document.getElementById("user-search");
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
	        