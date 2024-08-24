package com.web.clothes.ClothesWeb.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.requestDto.LoginRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.AuthenticationResponseDto;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.jwt.CustomUserDetails;
import com.web.clothes.ClothesWeb.jwt.JwtTokenProvider;
import com.web.clothes.ClothesWeb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/login")
public class LoginController {
	private final UserRepository userRepository;

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider tokenProvider;
	
	private static Log log = LogFactory.getLog(LoginController.class);
	
	@GetMapping()
	public String authenticateUser(Model model) {
		
		log.info("Check if user login or not when access login page ");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
System.out.println("sss:  "+authentication);
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("loginRequestDto", new LoginRequestDto());
			log.info("Get login view");
			return "users/login";
		}
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

		if (roles.contains("ADMIN")) {
			log.info("Get order view for admin");
			// Nếu đây là một quản trị viên, điều hướng về trang đăng nhập.
			return "redirect:/order/admin/all";
		} else {
			log.info("Get home view for user");
			// Nếu đây là một người dùng bình thường, điều hướng về trang chủ.
			return "redirect:/product/user-home";
		}

	}

	@PostMapping(value = "/checkLogin")
	@ResponseBody
	public ResponseEntity<?> authenticateUser1(HttpSession session,@Valid @RequestBody LoginRequestDto loginRequestDto,
			BindingResult bindingResult, Model model) {
		log.info("Check login");
		Map<String, Object> errors = new HashMap<>();
		if(bindingResult.hasErrors()) {
			
			log.info("Check login has error "+bindingResult.getAllErrors());
			
			errors.put("bindingErrors", bindingResult.getAllErrors());
			return ResponseEntity.badRequest().body(errors);
		}
		AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();

		Optional<User> user = userRepository.findUserByEmail(loginRequestDto.getEmail());
		
		if (!user.isPresent()) {
			log.error("check login has error 'The account with email is not exist'");
			errors.put("EmailErrors", "The account with email is not exist");
			return ResponseEntity.badRequest().body(errors);
		}

		if (user.isPresent() && !user.get().isActive()) {
			
			log.error("check login has error account with User "+user.get().getId()+ " not been verified");
			authenticationResponseDto.setMessage(
					"The account has not been verified, please check your email to verify your account before logging in");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authenticationResponseDto);
			
		}
		// Authenticate from username and password.
		Authentication authentication = null;

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
			
			
		} catch (BadCredentialsException ex) {
			errors.put("PasswordErrors", "The password is incorrect, please check again");
			return ResponseEntity.badRequest().body(errors);

		}

		// If no exception occurs, the information is valid
		// Set authentication information to Security Context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// generate token
		String token = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
		
		// get role
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		// check if user is user or admin
		String role = null;
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ADMIN")) {
				role = "ADMIN";
				break;
			} else if (authority.getAuthority().equals("USER")) {
				role = "USER";
				break;
			}
		}
		session.setAttribute("userName",user.get().getUserName());
		session.setAttribute("role",role);
		session.setAttribute("email",user.get().getEmail());
		model.addAttribute("userName", (String) session.getAttribute("userName"));
		model.addAttribute("role", (String) session.getAttribute("role"));
		authenticationResponseDto.setToken(token);
		authenticationResponseDto.setRole(role);

		log.info("Login successful.");
		return ResponseEntity.ok(authenticationResponseDto);
	}
}
