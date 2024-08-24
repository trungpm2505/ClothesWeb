package com.web.clothes.ClothesWeb.controller;


import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.CartRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.CartResponseDto;
import com.web.clothes.ClothesWeb.service.CartService;
import com.web.clothes.ClothesWeb.service.OrderService;
import com.web.clothes.ClothesWeb.service.OrderDetailsService;
import com.web.clothes.ClothesWeb.service.UserService;
import com.web.clothes.ClothesWeb.entity.Cart;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.OrderDetails;
import com.web.clothes.ClothesWeb.entity.User;


import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
	
	private final CartService cartService;
	private final Mapper mapper;
	private final UserService userService;
	private final OrderDetailsService  orderDetailsService;
	private final OrderService  orderService;
	private static Log log = LogFactory.getLog(CartController.class);
	
	//view product details
	@GetMapping()
	public String getViewCart(HttpSession session,Model model) {
		log.info("Get view cart with userName: " + (String) session.getAttribute("userName"));
		
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		return "users/cartform";
	}
	
		
	@PostMapping(value="/save" ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> save(@Valid @RequestBody CartRequestDto cartRequestDto,BindingResult bindingResult) {
		log.info("Save cart with userId: " + cartRequestDto.getUserId());
		
		if (bindingResult.hasErrors()) {
			//return new RedirectView("/product/details?productId="+cartRequestDto.getProductId());
		}
		
		Cart cart = mapper.cartRequestDtoToCart(cartRequestDto);
		cartService.save(cart);
		
		log.info("Save cart with userId: " + cartRequestDto.getUserId()+" successful");
		return ResponseEntity.ok().body("Add cart success!");
	
	}
	
	@PostMapping(value="/addByOrder" ,consumes = MediaType.APPLICATION_JSON_VALUE)
	//@ResponseBody
	public ResponseEntity<String> addByOrder(@RequestParam Integer orderId ) {
		log.info("Save cart By orderId: " + orderId);
		
		Optional<Order> order=orderService.getOrderById(orderId);
		List<OrderDetails> orderDetailsList = orderDetailsService.getOrderDtails(order.get());
		
		for (OrderDetails orderDetails : orderDetailsList) {
			CartRequestDto cartRequestDto= new CartRequestDto();
			cartRequestDto.setNumber(orderDetails.getNumber());
			cartRequestDto.setProductId(orderDetails.getProduct().getId());
			Cart cart = mapper.cartRequestDtoToCart(cartRequestDto);
			cartService.save(cart);
		}
		
		log.info("Save cart By orderId: " + orderId+" successful");
		return ResponseEntity.ok().body("Add cart success!");
	
	}
	
	
	@GetMapping(value="/findAll" ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CartResponseDto>> findByUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userService.findUserByEmail(authentication.getName());
		
		log.info("Get all cart value for userId" + user.get().getId());
		
		List<Cart> carts = cartService.findCartByUser(user.get());
		List<CartResponseDto> cartResponseDtos = new ArrayList<>();
		CartResponseDto cartResponseDto=null;
		for (Cart cart : carts) {
			 cartResponseDto = mapper.cartToCartResponseDto(cart);
			 cartResponseDtos.add(cartResponseDto);
		}
		
		log.info("Get all cart value for userId" + user.get().getId()+" successful");
		return ResponseEntity.ok().body(cartResponseDtos);
	
	}
	
	@PutMapping(value="/updateQuantity" )
	@ResponseBody
	public ResponseEntity<?> updateQuantity(@RequestParam Integer cartId,@RequestParam int quantity) {
		Optional<Cart> cart = cartService.getCart(cartId);
		
		log.info("Update quantity for cart " + cart.get().getId());
		
		cart.get().setNumber(quantity);
		
		if(cart.isEmpty()) {
			log.error("Update quantity for cart " + cart.get().getId()+" has error 'cart not exist'");
			return ResponseEntity.badRequest().body("This product does not exist in your cart!");
		}
		
		cartService.save(cart.get());
		log.info("Update quantity for cart " + cart.get().getId()+" successful");
		return ResponseEntity.ok().body("Successful!");
	
	}
	
	@DeleteMapping(value="/delete" )
	@ResponseBody
	public ResponseEntity<?> deleteCart(@RequestParam Integer cartId) {
		Optional<Cart> cart = cartService.getCart(cartId);
		
		log.info("Delete cart " + cart.get().getId());
		
		if(cart.isEmpty()) {
			log.error("Delete cart " + cart.get().getId()+" has error 'cart not exist'");
			return ResponseEntity.badRequest().body("This product does not exist in your cart!");
		}
		
		cartService.deleteCart(cart.get().getId());
		
		log.info("Delete cart " + cart.get().getId()+" successful");
		
		return ResponseEntity.ok().body("Successful!");
	
	}
}
