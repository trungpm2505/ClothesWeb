package com.web.clothes.ClothesWeb.controller;


import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.OrderRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.UserResponseDto;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.Cart;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.entity.OrderDetails;
import com.web.clothes.ClothesWeb.service.CartService;
import com.web.clothes.ClothesWeb.service.OrderDetailsService;
import com.web.clothes.ClothesWeb.service.OrderService;
import com.web.clothes.ClothesWeb.service.ProductService;
import com.web.clothes.ClothesWeb.service.RateService;
import com.web.clothes.ClothesWeb.service.UserService;
import com.web.clothes.ClothesWeb.dto.responseDto.CartResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderDetailsResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderPageResponseDto;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/order")
public class OrderController {
	
	private final OrderService orderService;
	private final OrderDetailsService orderDetailsService;
	private final UserService userService;
	private final CartService cartService;
	private final ProductService productService;
	private final RateService rateService;
	private static Log log = LogFactory.getLog(OrderController.class);
	
	private final Mapper mapper;

	@GetMapping("/admin/all")
	public String viewOrder(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get all order view for "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return"admin/all_order";
	}
	
	@GetMapping("/admin/confirmed")
	public String viewConfirmOrder(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get confirmed order view for "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return"admin/confirmed_order";
	}
	
	@GetMapping("/admin/new")
	public String viewNewOrder(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get new order view for "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return"admin/new_order";
	}
	
	@GetMapping("/admin/canceled")
	public String viewCancelledOrder(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get canceled order view for "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return"admin/cancelled_order";
	}
	
	@GetMapping("/admin/completed")
	public String viewCompletedOrder(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get history order view for "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return"admin/history_order";
	}
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	LocalDate startDateParse=null;
	LocalDate endDateParse=null;
	LocalDate createDateParse=null;
	//get page order for admin
	@GetMapping(value = "/getOrderPage")
	@ResponseBody
	public ResponseEntity<OrderPageResponseDto> getOrderPageForAdmin(
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page,@RequestParam(required = false,defaultValue = "0") int status,
			@RequestParam(defaultValue = "false") boolean search,@RequestParam(required = false) String key,@RequestParam(required = false) String createAt,
			@RequestParam(required = false) String startDate,@RequestParam(required = false) String endDate,@RequestParam(required = false) String ortherTime
			) {
		
		log.info("Get order page value");
		
		Page<Order> orderPage=null;
		if(status==0) {
			if(search==false) {
				 orderPage = orderService.getOrderPage(page, size);
			}else {
				if(createAt!=""){
				createDateParse=LocalDate.parse(createAt, formatter) ;
				
				//orderPage = orderService.findByCreateAtAndKeyword(page, size,LocalDate.parse(createAt, formatter) ,key,null);
				}else if(startDate!="" || endDate!=""){
					
					if(startDate!="") {
						startDateParse=LocalDate.parse(startDate, formatter);
					}
					if(endDate!="") {
						endDateParse=LocalDate.parse(endDate, formatter);
					}
				}
				orderPage = orderService.findByDateRangeAndKey(page, size,createDateParse,startDateParse ,endDateParse ,key,0);
				
				
			}
			
		}else if(status!=0) {
			if(search==false) {
				orderPage = orderService.getOrderPageByStatus(page, size,status);
			}else {
				if(createAt!=""){
				createDateParse=LocalDate.parse(createAt, formatter) ;
				
				}else if(startDate!="" || endDate!=""){
					
					if(startDate!="") {
						startDateParse=LocalDate.parse(startDate, formatter);
					}
					if(endDate!="") {
						endDateParse=LocalDate.parse(endDate, formatter);
					}
				}
				orderPage = orderService.findByDateRangeAndKey(page, size,createDateParse,startDateParse ,endDateParse ,key,status);
				
			}
			
			
			
		}
		endDateParse=null;
		startDateParse=null;
		createDateParse=null;
		OrderPageResponseDto orderPageResponseDto = new OrderPageResponseDto();
		List<OrderResponseDto> orderResponseDtos=new ArrayList<>();
		if(orderPage!=null) {
			 orderResponseDtos = orderPage.stream()
					.map(order -> new OrderResponseDto(order.getId(),
							order.getFullName(),order.getPhone(),order.getAdrress(),order.getCreateAt(),order.getTotalMoney()
								,order.getStatus(),order.getNote(),order.getUser().getUserName(),rateService.getRateByOrder(order).size()!=0? true : false))
					.collect(Collectors.toList());
			 
			 orderPageResponseDto.setTotalPages(orderPage.getTotalPages());
			 orderPageResponseDto.setSize(orderPage.getSize());
			 orderPageResponseDto.setOrderResponseDtos(orderResponseDtos);
			 orderPageResponseDto.setCurrentPage(orderPage.getNumber());
			
					
		}
		

		
		log.info("Get order page value successful");
		return ResponseEntity.ok(orderPageResponseDto);

	}
		
	//update status
	@PutMapping(value="/updateStatus")
	public ResponseEntity<?> updateStatus(@RequestParam Integer orderId,@RequestParam int status,@RequestParam(defaultValue = "false") boolean undo) {
		
		Optional<Order> order = orderService.getOrderById(orderId);
		
		log.info("Update status for order: "+order.get().getId());
		if(order.isEmpty()) {
			return ResponseEntity.badRequest().body("Order does not exist.");
		}
		orderService.updateOrderStatus(order.get(), status);
		
		if(status==4) {
			log.info("Solve for completed order: "+order.get().getId());
			order.get().setCompletedAt(LocalDate.now());
			orderService.save(order.get());
			List<OrderDetails> OrderDetailsList = orderDetailsService.getOrderDtails(order.get());
			for (OrderDetails orderDetails : OrderDetailsList) {
				orderDetails.getProduct().setQuantity(orderDetails.getProduct().getQuantity() - orderDetails.getNumber() );
				productService.save(orderDetails.getProduct());
			}
		}else if(undo==true){
			log.info("Solve undo for order: "+order.get().getId());
			order.get().setCompletedAt(null);
			List<OrderDetails> OrderDetailsList = orderDetailsService.getOrderDtails(order.get());
			for (OrderDetails orderDetails : OrderDetailsList) {
				orderDetails.getProduct().setQuantity(orderDetails.getNumber() + orderDetails.getProduct().getQuantity());
				productService.save(orderDetails.getProduct());
			}
		}
		
		log.info("Update status for order: "+order.get().getId()+" successful");
		return ResponseEntity.noContent().build();
		
	}
	
	
	Optional<Cart> cart;
	CartResponseDto cartResponseDto;
	
	@GetMapping("/checkout")
	public String createOrder(@RequestParam List<Integer> cartIdList,Model model ) {
		
		List<CartResponseDto> cartResponseDtos= new ArrayList<>();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userService.findUserByEmail(authentication.getName());
		
		log.info("Checkout order for user: "+user.get().getId());
		
		UserResponseDto userResponseDto = mapper.userToUserResponseDto(user.get());
		
		for (Integer id : cartIdList) {
			cart = cartService.getCart(id);
			cartResponseDto=mapper.cartToCartResponseDto(cart.get());
			cartResponseDtos.add(cartResponseDto);
		}
		model.addAttribute("userResponseDto",userResponseDto);
		model.addAttribute("cartResponseDtos",cartResponseDtos);
		model.addAttribute("orderRequestDto", new OrderRequestDto());
		
		log.info("Checkout order for user: "+user.get().getId()+" successful.");
		return"users/order";
	}
	
	
	
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<?> add(@Valid @RequestBody OrderRequestDto orderRequestDto,BindingResult bindingResult) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userService.findUserByEmail(authentication.getName());
		
		log.info("Save order for user: "+user.get().getId());
		
		Map<String, Object> errors = new HashMap<>();
		if (bindingResult.hasErrors()) {
			log.info("Save order for user: "+user.get().getId()+" has error "+ bindingResult.getAllErrors());
			errors.put("bindingErrors", bindingResult.getAllErrors());
			
		}
		if (!errors.isEmpty()) {
			return ResponseEntity.badRequest().body(errors);
		}
		
		
		Order order = new Order();
		order.setUser(user.get());
		order.setFullName(orderRequestDto.getFullName());
		order.setPhone(orderRequestDto.getPhone());
		order.setAdrress(orderRequestDto.getAddress());
		order.setNote(orderRequestDto.getNote());
		order.setTotalMoney(0);
		orderService.save(order);
		float totalPayment=0;
		
		for (Integer id : orderRequestDto.getCartIds()) {
			Optional<Cart> cart = cartService.getCart(id);
			OrderDetails orderDetails = new OrderDetails();
			System.out.println("slllllll "+cart.get().getNumber());
			orderDetails.setNumber(cart.get().getNumber());
			if(cart.get().getProduct().getCurrentPrice()!=null) {
				orderDetails.setCurentPrice(cart.get().getProduct().getCurrentPrice());
				orderDetails.setPrice(cart.get().getProduct().getPrice());
				orderDetails.setTotalMoney(cart.get().getNumber()*cart.get().getProduct().getCurrentPrice());
			}else {
				orderDetails.setPrice(cart.get().getProduct().getPrice());
				orderDetails.setTotalMoney(cart.get().getNumber()*cart.get().getProduct().getPrice());
			}
			orderDetails.setProduct(cart.get().getProduct());
			orderDetails.setOrder(order);
			orderDetailsService.save(orderDetails);
			totalPayment+=(orderDetails.getTotalMoney());
		}
		totalPayment+=30000;
		order.setTotalMoney(totalPayment);
		orderService.save(order);
		for (Integer id : orderRequestDto.getCartIds()) {
			cartService.deleteCart(id);
		}
		
		log.info("Save order for user: "+user.get().getId()+" Successful.");
		return ResponseEntity.ok("Success");
	}
	
	@GetMapping("/order-success")
	public String view(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "users/order-success";
	}
	//get page order for admin
		@GetMapping(value = "/user/getOrderPage")
		@ResponseBody
		public ResponseEntity<OrderPageResponseDto> getOrderPageForUser(
				@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int page,@RequestParam(required = false,defaultValue = "0") int status,@RequestParam(required = false) String keyWord) {
			Page<Order> orderPage=null;
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Optional<User> user = userService.findUserByEmail(authentication.getName());
			
			log.info("Get order page for user: "+user.get().getId());
			
			if(keyWord==null || keyWord.equals("")) {
				if(status==0) {
					 orderPage = orderService.getOrderPageByUser(page, size,user.get());
				}else if(status!=0) {
					orderPage = orderService.getOrderPageByStatusAndUser(page, size,status,user.get());
				}
			}else {
				orderPage = orderService.getOrderPageByKeywordAndUser(page, size,keyWord,user.get());
			}
			
			
			
			List<OrderResponseDto> orderResponseDtos = orderPage.stream()
					.map(order -> new OrderResponseDto(order.getId(),
							order.getFullName(),order.getPhone(),order.getAdrress(),order.getCreateAt(),order.getTotalMoney()
								,order.getStatus(),order.getNote(),order.getUser().getUserName(),rateService.getRateByOrder(order).size()!=0? true : false))
					.collect(Collectors.toList());
			
			
			for (int i = 0; i < orderPage.getContent().size(); i++) {
				List<OrderDetails> OrderDetails =orderDetailsService.getOrderDtails(orderPage.getContent().get(i));
				List<OrderDetailsResponseDto> orderDetailsResponseDtos=mapper.listOrderDtailsToListOrderDetailsDto(OrderDetails);
				orderResponseDtos.get(i).setOrderDetaitsResponseDtos(orderDetailsResponseDtos);
				
			}
			
			
			
			OrderPageResponseDto orderPageResponseDto = new OrderPageResponseDto(
					orderPage.getTotalPages(), orderPage.getNumber(), orderPage.getSize(),
					orderResponseDtos);
			
			log.info("Get order page for user: "+user.get().getId()+" Successful.");
			return ResponseEntity.ok(orderPageResponseDto);

		}
	@GetMapping("/all-order")
	public String getAllOrderForUser(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get all order view for user: "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "users/allOrder";
	}
	
	@GetMapping("/confirmed-order")
	public String getConfirmOrderForUser(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get confirmed-order view for user: "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "users/confirmed_order";
	}
	
	@GetMapping("/new-order")
	public String getNewOrderForUser(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get new-order view for user: "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "users/newOrder";
	}
	
	@GetMapping("/canceled-order")
	public String getCanceledOrderForUser(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get canceled-order view for user: "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "users/canceledOrder";
	}
	
	@GetMapping("/completed-order")
	public String getCompletedOrderForUser(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		
		log.info("Get completed-order view for user: "+userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "users/historyOrder";
	}
	
	
	
	
	
}
