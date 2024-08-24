package com.web.clothes.ClothesWeb.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.repository.OrderRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
	
	private final OrderRepository orderRepository;

	@Override
	public Page<Order> getOrderPage(int pageNumber, int szie) {
		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
		
		 Page<Order> orderPage = orderRepository.findAll(orderPageable);
		 
		return orderPage;
	}

	@Override
	public Page<Order> getOrderPageByStatus(int pageNumber, int szie, int status) {
		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
		 Page<Order> orderPage = orderRepository.findOrderByStatus(orderPageable,status);
		return orderPage;
	}

	@Override
	public Optional<Order> getOrderById(Integer orderId) {
		Optional<Order> order = orderRepository.getOrderById(orderId);
		return order;
	}

	@Override
	public void updateOrderStatus(Order order,int status) {
		
		order.setStatus(status);
		orderRepository.save(order);
		
	}

	@Override
	public void save(Order order) {
		orderRepository.save(order);
	}

	@Override
	public Page<Order> getOrderPageByUser(int pageNumber, int szie, User user) {
		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findByUser(orderPageable, user);
	}

	@Override
	public Page<Order> getOrderPageByStatusAndUser(int pageNumber, int szie, int status,User user) {
		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findOrderByStatusAndUser(orderPageable,status, user);
	}

//	@Override
//	public Page<Order> findByKeyword(int pageNumber, int szie, String key) {
//		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
//		return orderRepository.findByKeyword(orderPageable,key);
//	}

	@Override
	public Page<Order> findByDateRangeAndKey(int pageNumber, int szie,LocalDate createAt, LocalDate startDate, LocalDate endDate,
			String key,int status) {
		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findByDateRangeAndKeyword(orderPageable,createAt,startDate,endDate,key,status);
	}

	@Override
	public Page<Order> findOrderForReport(int pageNumber, int szie, LocalDate completedAt, LocalDate startDate,
			LocalDate endDate, String key) {
		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "completedAt").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findOrderForReport(orderPageable,completedAt,startDate,endDate,key);
	}

	@Override
	public Page<Order> getOrderPageByKeywordAndUser(int pageNumber, int szie, String keyWord, User user) {
		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
		return orderRepository.findByKeywordForUser(orderPageable, user,keyWord);
	}

//	@Override
//	public Page<Order> findByCreateAtAndKeyword(int pageNumber, int szie, LocalDate createAt, String key,String status) {
//		PageRequest orderPageable = PageRequest.of(pageNumber, szie, Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id")));
//		return orderRepository.findByCreateAtAndKeyword(orderPageable,createAt,key,status);
//	}


}
