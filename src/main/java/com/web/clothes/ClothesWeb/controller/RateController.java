package com.web.clothes.ClothesWeb.controller;

import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.RateRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.RatePageResponseDto;
import com.web.clothes.ClothesWeb.service.ImageService;
import com.web.clothes.ClothesWeb.service.OrderService;
import com.web.clothes.ClothesWeb.service.ProductService;
import com.web.clothes.ClothesWeb.service.RateService;
import com.web.clothes.ClothesWeb.service.ResponseService;
import com.web.clothes.ClothesWeb.service.UserService;
import com.web.clothes.ClothesWeb.entity.Rate;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.dto.responseDto.RateResponseDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/rate")
public class RateController {
	private final RateService rateService;
	private final ImageService imageService;
	private final UserService userService;
	private final ProductService productService;
	private final Mapper mapper;
	private final ResponseService responseService;
	private final OrderService orderService;
	private static Log log = LogFactory.getLog(RateController.class);
	
	@PostMapping(value = "/add", consumes = "multipart/form-data", produces = { "application/json", "application/xml" })
	@Transactional
	@ResponseBody
	public ResponseEntity<?> upload(@RequestParam(value = "file", required = false) MultipartFile[] files,
			@Valid @ModelAttribute RateRequestDto rateRequestDto) {
		log.info("Add rate");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> user = userService.findUserByEmail(authentication.getName());
		
		Rate rate = mapper.mapRateRequestDtoToRate(rateRequestDto,user.get());
		
		rateService.save(rate);
		
			if(rateRequestDto.getRateId()!=null && imageService.getImageByRate(rate).size()!=0) {
				
				imageService.deleteByRate(rate);
			}
			// image
			if(files!=null) {
				imageService.uploadFileForRate(files, rate);
			}

		log.info("Add rate successful");
		return ResponseEntity.ok().body("{\"message\": \"Rating successfully\"}");
	}
	
	@GetMapping(value = "/getRatePage")
	@ResponseBody
	public ResponseEntity<RatePageResponseDto> getProductPage(
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "0") int page,@RequestParam Integer productId,@RequestParam(required = false, defaultValue = "0") int rateScore) {
		log.info("Add rate page value");
		Page<Rate> ratePage = null;
			if(rateScore==0) {
				ratePage = rateService.finPage(page, size, productService.getProduct(productId).get());
			}else {
				ratePage = rateService.finPageByRateScore(page, size, productService.getProduct(productId).get(),rateScore);
			}
	
		List<RateResponseDto> rateResponseDtos = ratePage.stream()
				.map(rate -> new RateResponseDto(rate.getId(),
						rate.getCreateAt(),rate.getRating(),rate.getContent(),mapper.userToUserResponseDto(rate.getUser()),responseService.getAll(rate),imageService.getImageByRate(rate)))
				.collect(Collectors.toList());
		
		
		RatePageResponseDto ratePageResponseDto = new RatePageResponseDto(
				ratePage.getTotalPages(), ratePage.getNumber(), ratePage.getSize(),
				rateResponseDtos);
		
		log.info("Add rate page value successful");
		return ResponseEntity.ok(ratePageResponseDto);

	}
	
	@GetMapping(value = "/getByOrder")
	@ResponseBody
	public  ResponseEntity<RateResponseDto>  getProductByOrder(
			@RequestParam Integer orderId,@RequestParam Integer productId) {
		log.info("Add rate by order");
		Rate rate= rateService.finPageByOrderAndProduct(orderService.getOrderById(orderId).get(),productService.getProduct(productId).get());
	
		RateResponseDto rateResponseDtos = new RateResponseDto();
		rateResponseDtos.setId(rate.getId());
		rateResponseDtos.setCreateAt(rate.getCreateAt());
		rateResponseDtos.setContent(rate.getContent());
		rateResponseDtos.setRating(rate.getRating());
		rateResponseDtos.setUserResponseDto(mapper.userToUserResponseDto(rate.getUser()));
		rateResponseDtos.setResponses(responseService.getAll(rate));
		rateResponseDtos.setImages(imageService.getImageByRate(rate));
		
		log.info("Add rate by order successful");
		return ResponseEntity.ok(rateResponseDtos);

	}
}
