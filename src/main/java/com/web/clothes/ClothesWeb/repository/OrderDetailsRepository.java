package com.web.clothes.ClothesWeb.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.OrderDetails;
import com.web.clothes.ClothesWeb.entity.Product;
@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Integer>{
	
	public List<OrderDetails> findOrderDetailsByOrder(Order order);
	
	@Query("SELECT count(o.number) FROM OrderDetails o WHERE o.product = :product AND o.order.status = 4")
	public int getQuantityOfSold(Product product);
	
	@Query("SELECT count(o.number) FROM OrderDetails o WHERE o.product.category = :category AND o.order.status = 4")
	public int getQuantityOfProductAndCategory(Category category);
	
	@Query("SELECT count(*) FROM OrderDetails o WHERE o.order = :order")
	public int getNumberOfProductInOrder(Order order); 
	
	
}
