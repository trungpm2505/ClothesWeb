
 	var id;
  	function redirectToDetail(event) {
		id =  event.currentTarget.querySelector('.product-id').value;
        window.location.href = '/product/details?productId='+ id ;
    }