package com.web.clothes.ClothesWeb.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.web.clothes.ClothesWeb.dto.responseDto.OrderPageResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderResponseDto;
import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.service.CategoryService;
import com.web.clothes.ClothesWeb.service.OrderDetailsService;
import com.web.clothes.ClothesWeb.service.OrderService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/report")
public class ReportController {
	private final OrderService orderService;
	private final UserService userService;
	private final CategoryService categoryService;
	private final OrderDetailsService orderDetailsService;
	private static Log log = LogFactory.getLog(ReportController.class);
	
	@GetMapping()
	public String viewReport(Model model) {
		log.info("Get view report");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		return"admin/report";
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	LocalDate startDateParse=null;
	LocalDate endDateParse=null;
	LocalDate completedDateParse=null;
	//get page order for admin
	@GetMapping(value = "/getOrderPage")
	@ResponseBody
	public ResponseEntity<OrderPageResponseDto> getOrderPageForAdmin(
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "false") boolean search,@RequestParam(required = false) String key,@RequestParam(required = false) String completedAt,
			@RequestParam(required = false) String startDate,@RequestParam(required = false) String endDate
			) {
		log.info("Get report page value");
		Page<Order> orderPage=null;
		
			
				if(completedAt!=""){
					completedDateParse=LocalDate.parse(completedAt, formatter) ;
				
				}else if(startDate!="" || endDate!=""){
					
					if(startDate!="") {
						startDateParse=LocalDate.parse(startDate, formatter);
					}
					if(endDate!="") {
						endDateParse=LocalDate.parse(endDate, formatter);
					}
				}
				orderPage = orderService.findOrderForReport(page, size,completedDateParse,startDateParse ,endDateParse ,key);
				
				
			
			
		
			
		endDateParse=null;
		startDateParse=null;
		completedDateParse=null;
		OrderPageResponseDto orderPageResponseDto = new OrderPageResponseDto();
		List<OrderResponseDto> orderResponseDtos=new ArrayList<>();
		if(orderPage!=null) {
			 orderResponseDtos = orderPage.stream()
					.map(order -> new OrderResponseDto(order.getId(),
							order.getFullName(),order.getPhone(),order.getAdrress(),order.getCreateAt(),order.getCompletedAt(),order.getTotalMoney()
								,order.getStatus(),order.getNote(),order.getUser().getUserName()))
					.collect(Collectors.toList());
			 
			 orderPageResponseDto.setTotalPages(orderPage.getTotalPages());
			 orderPageResponseDto.setSize(orderPage.getSize());
			 orderPageResponseDto.setOrderResponseDtos(orderResponseDtos);
			 orderPageResponseDto.setCurrentPage(orderPage.getNumber());
					
		}
		log.info("Get report page value sucessful");
		return ResponseEntity.ok(orderPageResponseDto);

	}
	
	@GetMapping("/soldOfCategory")
	@ResponseBody
	public ResponseEntity<Map<String, Integer>> getQuantityOfProductAndCategory() {
		
		log.info("Get soldOfCategory sucessful");
		Map<String,Integer> soleOfCatehory = new HashMap<String,Integer>();
		List<Category> categories = categoryService.getAll();
		for (Category category : categories) {
			soleOfCatehory.put(category.getCategoryName(),orderDetailsService.getQuantityOfProductAndCategory(category));
		}
		
		return ResponseEntity.ok(soleOfCatehory);
	}
}
