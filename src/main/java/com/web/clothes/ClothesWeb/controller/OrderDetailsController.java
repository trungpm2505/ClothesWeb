package com.web.clothes.ClothesWeb.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderDetailsResponseDto;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.service.OrderDetailsService;
import com.web.clothes.ClothesWeb.service.OrderService;
import com.web.clothes.ClothesWeb.service.UserService;
import com.web.clothes.ClothesWeb.entity.OrderDetails;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderResponseDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/orderDetails")
public class OrderDetailsController {
	
	private final UserService userService;
	private final OrderService orderService;
	private final OrderDetailsService orderDetailsService;
	private final Mapper mapper;
	private static Log log = LogFactory.getLog(OrderDetailsController.class);
	
	@GetMapping("/get")
	public String getOrderDetails(@RequestParam Integer orderId,Model model) {
		Optional<Order> order = orderService.getOrderById(orderId);
		
		log.info("Get order details view for order: "+order.get().getId());
		
		OrderResponseDto orderResponseDto = mapper.orderToOrderResponseDto(order.get());
		
		List<OrderDetails> orderDetails = order.get().getOrderDetailsSet();

		List<OrderDetailsResponseDto> orderDetailsResponseDtos = mapper.listOrderDtailsToListOrderDetailsDto(orderDetails);
		int number= orderDetailsService.getNumberOfProductInOrder(order.get());
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		model.addAttribute("number", number);
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		model.addAttribute("orderResponseDto", orderResponseDto);
		model.addAttribute("orderDetailsResponseDtos", orderDetailsResponseDtos);
		

		if (roles.contains("ADMIN")) {
			log.info("Get order details view for order: "+order.get().getId()+" successful");
			return "admin/order_details";
		} else {
			log.info("Get order details view for order: "+order.get().getId()+" successful");
			// Nếu đây là một người dùng bình thường, điều hướng về trang chủ.
			return "users/orderDetail";
		}
		
	}
	
	
}
