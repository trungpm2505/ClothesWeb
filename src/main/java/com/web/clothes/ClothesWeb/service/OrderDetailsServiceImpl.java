package com.web.clothes.ClothesWeb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.OrderDetails;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.repository.OrderDetailsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService{
	
	private final OrderDetailsRepository orderDetailsRepository;

	@Override
	public List<OrderDetails> getOrderDtails(Order order) {
		return orderDetailsRepository.findOrderDetailsByOrder(order);
	}

	@Override
	public void save(OrderDetails orderDetails) {
		
		orderDetailsRepository.save(orderDetails);
	}

	@Override
	public int getQuantityOfSold(Product product) {
		return orderDetailsRepository.getQuantityOfSold(product);
	}

	@Override
	public int getNumberOfProductInOrder(Order order) {
		return orderDetailsRepository.getNumberOfProductInOrder(order);
	}

	@Override
	public int getQuantityOfProductAndCategory( Category category) {
		return orderDetailsRepository.getQuantityOfProductAndCategory(category);
	}
}
