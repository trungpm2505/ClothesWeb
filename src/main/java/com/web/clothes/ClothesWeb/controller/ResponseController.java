package com.web.clothes.ClothesWeb.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.requestDto.ResponseRequestDto;
import com.web.clothes.ClothesWeb.entity.ResponseRate;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.service.RateService;
import com.web.clothes.ClothesWeb.service.ResponseService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/response")
public class ResponseController {

	private final ResponseService responseService;
	private final RateService rateService;
	private final UserService userService;
	private static Log log = LogFactory.getLog(ResponseController.class);
	
	@PostMapping(value = "/add")
	@Transactional
	@ResponseBody
	public ResponseEntity<?> addResponse(HttpSession session,@Valid @RequestBody ResponseRequestDto responseRequestDto) {
		
		log.info("Save responseRate");
		String currentPrincipalName = (String) session.getAttribute("email");
	    Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
	    if(userByEmail.isEmpty()) {
	    	return ResponseEntity.badRequest().body("error");
	    
	    }
		ResponseRate responseRate = new ResponseRate();
		responseRate.setRate(rateService.getRateById(responseRequestDto.getRateId()));
		responseRate.setContent(responseRequestDto.getContent());
		responseRate.setUser(userByEmail.get());
		responseService.save(responseRate);
	
			
		log.info("Save responseRate sucessful");
		return ResponseEntity.ok().body("{\"message\": \"Response rate successfully\"}");
	}
}
