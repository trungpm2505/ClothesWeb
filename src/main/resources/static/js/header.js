//loadCategory();
function loadCategory() {
	
		   
		    
		    $.ajax({
		        url: "/category/getAll",
		        type: "GET",
		        dataType: 'json',
		        success: function(categories) {
		       	$('#category-slider').empty();
	    		  categories.forEach(function(category) {
	    			   $('#category-slider').append($('<div>').addClass('categoryForSearch').attr('data-id', category.id).text(category.name));
	    			});
	    		
	         	 createSlider();
	         	 clickForCategory();
		        },
		        error: function(jqXHR, textStatus, errorMessage) {
		            var error = jqXHR.responseJSON || jqXHR.responseText;
		            
		        }
		    });
		}
		
		loadCategory() ;
		
		function createSlider(){
			$(".category").slick({
					    slidesToShow: 4,
					    slidesToScroll: 1,
					    infinite: true,
					    arrows: true,
					    draggable: false,
					    
					   
					    prevArrow: `<button type='button' class='slick-prev slick-arrow rounded-circle slick-prev-category'><i class="fa fa-chevron-left"></i></button>`,
					    nextArrow: `<button type='button' class='slick-next slick-arrow rounded-circle slick-next-category'><i class="fa fa-chevron-right"></i></button>`,
					    dots: false,
					   initialSlide: 0,
					   slidesPerView: 1,
					   autoplay: false,
					    responsive: [
					      {
					        breakpoint: 1025,
					        settings: {
					          slidesToShow: 3,
					        },
					      },
					      {
					        breakpoint: 480,
					        settings: {
					          slidesToShow: 1,
					          arrows: false,
					          infinite: false,
					        },
					      },
					    ],
					  
					    // autoplaySpeed: 1000,
					  });
		}
		
		function clickForCategory(){
			//click for category
			var categories = document.getElementsByClassName("categoryForSearch");
			
			for (var i = 0; i < categories.length; i++) {
		      categories[i].addEventListener("click", function() {
				 
		        if(this.getAttribute('data-id') !== null || this.getAttribute('data-id') !== ""){
					 
					 window.location.href = "/product/searchByCategory?category="+ this.getAttribute('data-id');
					 
				 }
		      });
		   }
		}
		
		//button search by key
		var searchButton = document.getElementById("search");
		
		searchButton.addEventListener("click", function() {
			
			 if($('#key').val() !== ""){
				 
				 window.location.href = "/product/search?key="+$('#key').val();
				 
			 }
		
		});
		
		
		
	    
		
		