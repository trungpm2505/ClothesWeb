package com.web.clothes.ClothesWeb.dto.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.web.clothes.ClothesWeb.dto.responseDto.CartResponseDto;
import com.web.clothes.ClothesWeb.dto.requestDto.AttributeValueRequestDto;

import com.web.clothes.ClothesWeb.dto.requestDto.CartRequestDto;

import com.web.clothes.ClothesWeb.dto.requestDto.FeedbackRequestDto;

import com.web.clothes.ClothesWeb.dto.requestDto.ProductRequestDto;
import com.web.clothes.ClothesWeb.dto.requestDto.RateRequestDto;
import com.web.clothes.ClothesWeb.dto.requestDto.UserRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.OrderDetailsResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.ProductResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.UserResponseDto;
import com.web.clothes.ClothesWeb.entity.Attribute;
import com.web.clothes.ClothesWeb.entity.AttributeValue;
import com.web.clothes.ClothesWeb.entity.Cart;
import com.web.clothes.ClothesWeb.entity.Category;


import com.web.clothes.ClothesWeb.entity.Feedback;
import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.OrderDetails;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.Rate;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.service.AttributeService;
import com.web.clothes.ClothesWeb.service.CartService;
import com.web.clothes.ClothesWeb.service.CategoryService;
import com.web.clothes.ClothesWeb.service.ImageService;
import com.web.clothes.ClothesWeb.service.OrderDetailsService;
import com.web.clothes.ClothesWeb.service.OrderService;
import com.web.clothes.ClothesWeb.service.ProductAttributeValueService;
import com.web.clothes.ClothesWeb.service.ProductService;
import com.web.clothes.ClothesWeb.service.RateService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Mapper {
	private final AttributeService attributeService;
	private final CategoryService categoryService;
	private final ImageService imageService;
	private final ProductAttributeValueService productAttributeValueService;
	private final ProductService productService;
	private final UserService userService;
	private final OrderDetailsService orderDetailsService;
	private final CartService cartService;
	private final OrderService orderService;
	private final RateService rateService;

	public User userRquestDtoMapToUser(UserRequestDto userRequestDto) {
		User user = new User();
		user.setUserName(userRequestDto.getUsername());
		user.setPassword(userRequestDto.getPassword());
		user.setFullName(userRequestDto.getFullname());
		user.setAddress(userRequestDto.getAddress());
		user.setPhone(userRequestDto.getPhone());
		user.setEmail(userRequestDto.getEmail());
		return user;

	}
	
	public UserResponseDto userToUserResponseDto(User user) {
		UserResponseDto  userResponseDto = new UserResponseDto();
		userResponseDto.setId(user.getId());
		userResponseDto.setUserName(user.getUserName());
		userResponseDto.setFullName(user.getFullName());
		userResponseDto.setAddress(user.getAddress());
		userResponseDto.setPhone(user.getPhone());
		userResponseDto.setEmail(user.getEmail());
		userResponseDto.setRoleName(user.getRole().getRoleName());
		return userResponseDto;

	}

	public AttributeValue attributeValueRequestDtoToAttributeValue(AttributeValueRequestDto attributeValueRequestDto) {
		AttributeValue attributeValue = new AttributeValue();
		attributeValue.setAttributeValueName(attributeValueRequestDto.getAttributeValueName());
		Optional<Attribute> attribute = attributeService.getAttribute(attributeValueRequestDto.getAttributeName());
		attributeValue.setAttribute(attribute.get());

		return attributeValue;

	}

	public Product productRequestDtoToProduct(ProductRequestDto productRequestDto) {
		Product product = new Product();
		if (productRequestDto.getId() != null) {
			product.setId(productRequestDto.getId());
		}
		// set title
		product.setTitle(productRequestDto.getTitle());

		// set category
		Optional<Category> category = categoryService.getCategory(productRequestDto.getCategory());
		product.setCategory(category.get());

		// set price
		product.setPrice(productRequestDto.getPrice());

		// set current price
		if (productRequestDto.getCurrentPrice() != 0) {
			product.setCurrentPrice(productRequestDto.getCurrentPrice());
		}

		// set quantity
		product.setQuantity(productRequestDto.getQuantity());

		// set gender
		product.setGender(Boolean.parseBoolean(productRequestDto.getGender()));

		// set discription
		product.setDiscription(productRequestDto.getDescription());

		return product;

	}

	public ProductResponseDto productToProductResponese(Product product) {
		ProductResponseDto productResponse = new ProductResponseDto();

		productResponse.setId(product.getId());

		// set title
		productResponse.setId(product.getId());

		// set title
		productResponse.setTitle(product.getTitle());

		// set category
		Optional<Category> category = categoryService.getCategory(product.getCategory().getId());
		productResponse.setCategoryName(category.get().getCategoryName());

		// set price
		productResponse.setPrice(product.getPrice());
		
		

		// set current price
		if (product.getCurrentPrice() != null) {
			productResponse.setCurrentPrice(product.getCurrentPrice());
		}

		// set quantity
		productResponse.setQuantity(product.getQuantity());
		
		productResponse.setQuantityOfSold(orderDetailsService.getQuantityOfSold(product));

		// set gender
		productResponse.setGender((product.isGender()));

		// set discription
		productResponse.setDescription(product.getDiscription());

		// set images
		productResponse.setImages(imageService.getImageByProduct(product));

		// set Attribute
		productResponse.setProductAttributeValue(
				productAttributeValueService.findProductAttributeValueByProduct(product) != null
						? productAttributeValueService.findProductAttributeValueByProduct(product)
						: null);

		return productResponse;

	}

	public OrderResponseDto orderToOrderResponseDto(Order order) {
		OrderResponseDto orderResponseDto = new OrderResponseDto();
		orderResponseDto.setId(order.getId());
		orderResponseDto.setFullName(order.getFullName());
		orderResponseDto.setPhone(order.getPhone());
		orderResponseDto.setAddress(order.getAdrress());
		orderResponseDto.setCreateAt(order.getCreateAt());
		orderResponseDto.setCompletedAt(order.getCompletedAt());
		orderResponseDto.setTotalMoney(order.getTotalMoney());
		orderResponseDto.setStatus(order.getStatus());
		orderResponseDto.setNote(order.getNote());
		return orderResponseDto;
	}

	
	public Cart cartRequestDtoToCart(CartRequestDto cartRequestDto) {
		
		
		Optional<Product> product = productService.getProduct(cartRequestDto.getProductId());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userService.findUserByEmail(authentication.getName());
		
		//if product and user id is exist in cart -> update
		Optional<Cart> cartExist =cartService.findByProductAndUser(product.get(), user.get());
		if(cartExist.isPresent()) {
			if(cartRequestDto.getNumber()!=0) {
				cartExist.get().setNumber(cartExist.get().getNumber()+cartRequestDto.getNumber());
			}else{
				cartExist.get().setNumber(cartExist.get().getNumber()+1);
			}
			//set update at 
			cartExist.get().setUpdatedAt(LocalDateTime.now());
			return cartExist.get();
		}else {
			Cart cart = new Cart();
			//set Number
			if(cartRequestDto.getNumber()!=0) {
				cart.setNumber(cartRequestDto.getNumber());
			}else{
				cart.setNumber(1);
			}
			
			
			//set Product
			cart.setProduct(product.get());
			
			//set user
			cart.setUser(user.get());
			return cart;
		}
			
	}
	

	
	Optional<Product> product=null;
	public CartResponseDto cartToCartResponseDto(Cart cart) {
		CartResponseDto cartResponseDto= new CartResponseDto();
		 product = productService.getProduct(cart.getProduct().getId());
		cartResponseDto.setCartId(cart.getId());
		cartResponseDto.setQuantity(cart.getNumber());
		cartResponseDto.setProductResponseDto(productToProductResponese(product.get()));
		cartResponseDto.setPrice(product.get().getPrice());
		cartResponseDto.setCurrentPrice(product.get().getCurrentPrice());
		if(product.get().getCurrentPrice()!=null) {
			cartResponseDto.setTotal(cart.getNumber()*product.get().getCurrentPrice());
		}else {
			cartResponseDto.setTotal(cart.getNumber()*product.get().getPrice());
		}
		
		cartResponseDto.setProductAttributeValue(productAttributeValueService.findProductAttributeValueByProduct(product.get())!=null ? productAttributeValueService.findProductAttributeValueByProduct(product.get()):null);
		cartResponseDto.setImages(imageService.getImageByProductAndDefault(product.get()));
		return cartResponseDto;
	}
	
	public List<OrderDetailsResponseDto> listOrderDtailsToListOrderDetailsDto(List<OrderDetails> OrderDetailsList) {
		List<OrderDetailsResponseDto> orderDetailsResponseDtos = new ArrayList<>();
		for (OrderDetails orderDatils : OrderDetailsList) {
			OrderDetailsResponseDto orderDetailsResponseDto = new OrderDetailsResponseDto();
		
			orderDetailsResponseDto.setNumber(orderDatils.getNumber());
			orderDetailsResponseDto.setPrice(orderDatils.getPrice());
			
			if(orderDatils.getCurentPrice()!=0) {
				orderDetailsResponseDto.setCurentPrice(orderDatils.getCurentPrice());
			}
			orderDetailsResponseDto.setProductResponseDto(productToProductResponese( orderDatils.getProduct()));
			
			orderDetailsResponseDto.setProductAttributeValue(productAttributeValueService.findProductAttributeValueByProduct(orderDatils.getProduct()));
			
			
			orderDetailsResponseDto.setTotalMoney(orderDatils.getTotalMoney());
			orderDetailsResponseDto.setImageForSave(imageService.getImageByProductAndDefault(orderDatils.getProduct()).getInmageForSave());
			orderDetailsResponseDtos.add(orderDetailsResponseDto);
		}
		return orderDetailsResponseDtos;
	}
	



	

	public Feedback feebackRequestDtoToFeedback(FeedbackRequestDto feedbackRequestDto) {
		Feedback feedback = new Feedback();
		if (feedbackRequestDto.getId() != null) {
			feedback.setId(feedbackRequestDto.getId());
		}

		feedback.setEmail(feedbackRequestDto.getEmail());
		feedback.setFullName(feedbackRequestDto.getFullName());
		feedback.setPhone(feedbackRequestDto.getPhone());
		feedback.setSubjectName(feedbackRequestDto.getSubjectName());
		feedback.setNote(feedbackRequestDto.getNote());
		feedback.setCreateAt(LocalDate.now());
		return feedback;
	}
	
	public Rate mapRateRequestDtoToRate(RateRequestDto rateRequestDto,User user) {
		Rate rate=null;
		if(rateRequestDto.getRateId()!=null) {
			rate =  rateService.getRateById(rateRequestDto.getRateId());
			rate.setRating(rateRequestDto.getRating());	
			rate.setContent(rateRequestDto.getContent());
			
		}else {
			 rate = new Rate();
			rate.setUser(user);
			rate.setProduct(productService.getProduct(rateRequestDto.getProductId()).get());
			rate.setRating(rateRequestDto.getRating());	
			rate.setContent(rateRequestDto.getContent());
			rate.setOrder(orderService.getOrderById(rateRequestDto.getOrderId()).get());
		}
		
		return rate;
	}
}

