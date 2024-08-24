package com.web.clothes.ClothesWeb.service;

import java.util.List;

import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.OrderDetails;
import com.web.clothes.ClothesWeb.entity.Product;

public interface OrderDetailsService {
	public List<OrderDetails> getOrderDtails(Order order); 
	public void save(OrderDetails orderDetails); 
	public int getQuantityOfSold(Product product); 
	public int getNumberOfProductInOrder(Order order); 
	public int getQuantityOfProductAndCategory(Category category); 
	
}
