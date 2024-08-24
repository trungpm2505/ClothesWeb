package com.web.clothes.ClothesWeb.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.User;

public interface OrderService {
	
	public Page<Order> getOrderPage(int pageNumber, int szie);
	public Page<Order> getOrderPageByStatus(int pageNumber, int szie,int status);
	public Optional<Order> getOrderById(Integer orderId);
	public void updateOrderStatus(Order order,int status);
	public void save(Order order);
	

	public Page<Order> findByDateRangeAndKey(int pageNumber, int szie,LocalDate createAt,LocalDate startDate,LocalDate endDate,String key,int status);
	
	public Page<Order> findOrderForReport(int pageNumber, int szie,LocalDate completedAt,LocalDate startDate,LocalDate endDate,String key);
	
	public Page<Order> getOrderPageByUser(int pageNumber, int szie,User user);
	public Page<Order> getOrderPageByStatusAndUser(int pageNumber, int szie,int status,User user);
	public Page<Order> getOrderPageByKeywordAndUser(int pageNumber, int szie,String keyWord,User user);
}
