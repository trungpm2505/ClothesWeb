package com.web.clothes.ClothesWeb.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.UserRequestDto;
import com.web.clothes.ClothesWeb.entity.ConfirmationToken;
import com.web.clothes.ClothesWeb.entity.Role;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.service.ConfirmationTokenService;
import com.web.clothes.ClothesWeb.service.MailerService;
import com.web.clothes.ClothesWeb.service.RoleService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/register")
public class RegisterController {
	private static Log log = LogFactory.getLog(RegisterController.class);
	private final UserService userService;

	private final MailerService mailerService;

	private final ConfirmationTokenService confirmationTokenService;

	private final PasswordEncoder passwordEncoder;

	private static final String USER = "USER";

	private final RoleService roleService;

	private final Mapper mapper;

	@GetMapping()
	public String displayRegistration(Model model) {
		log.info("Get register view");
		model.addAttribute("userRequestDto", new UserRequestDto());
		return "users/register";
	}

	@Transactional
	@PostMapping(value = "/checkRegister")
	public String registerUser(Model model, @ModelAttribute("userRequestDto") @Valid UserRequestDto userRequestDto,
			BindingResult bindingResult) {
		log.info("Check register");
//		if (bindingResult.hasErrors()) {
//			return "users/register";
//		}
		Optional<User> UserByEmail = userService.findUserByEmail(userRequestDto.getEmail());
		Optional<User> UserByPhone = userService.findUserByPhone(userRequestDto.getPhone());
		Optional<User> UserByUserName = userService.findUserByUsername(userRequestDto.getUsername());
		Optional<Role> role = roleService.getRoleByName(USER);

		if (bindingResult.hasErrors() || UserByEmail.isPresent() || UserByPhone.isPresent() || UserByUserName.isPresent()
				|| !userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
			if (UserByEmail.isPresent()) {
				model.addAttribute("emailDuplicate", "Email already exists! Please enter a new one");
			}
			if (UserByPhone.isPresent()) {
				model.addAttribute("phoneDuplicate", "Phone already exists! Please enter a new one");
			}
			if (UserByUserName.isPresent()) {
				model.addAttribute("usernameDuplicate", "Username already exists! Please enter a new one");
			}
			if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword())) {
				model.addAttribute("notMatchPass", "Passwords do not match");
			}
			return "users/register";
		}

		String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
		userRequestDto.setPassword(encodedPassword);

		User user = mapper.userRquestDtoMapToUser(userRequestDto);
		user.setRole(role.get());
		userService.save(user);

		mailerService.sendEmailToConfirmAccount(user);

		model.addAttribute("email", user.getEmail());
		
		log.info("Check register successful");
		return "users/successfulRegisteration";
	}

	@PostMapping(value = "/resendMail")
	public String sendMailVerifyAccount(@RequestParam("email") String email, Model model) {
		log.info("Send email for register");
		
		Optional<User> user = userService.findUserByEmail(email);

		mailerService.sendEmailToConfirmAccount(user.get());
		model.addAttribute("email", user.get().getEmail());
		
		log.info("Send email for register successful");
		return "users/successfulRegisteration";
	}

	@RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
	public String confirmUserAccount(Model model, @RequestParam("token") String confirmationToken) {
		log.info("Check token for register");
		Optional<ConfirmationToken> token = confirmationTokenService.getConfirmationTokenByToken(confirmationToken);
		
		// if token is not valid
		if (!token.isPresent()) {
			model.addAttribute("message", "The link is invalid or broken!");
			return "users/verifyTokenError";
		}
		// if token is valid but expiryDate

		if (token.isPresent()) {

			if (token.get().getExpiryDate().isBefore(LocalDateTime.now())) {
				log.error("Check token for register has error 'The link is invalid or broken'");
				
				model.addAttribute("message", "The link is invalid or broken!");
				return "users/verifyTokenError";
			}
			Optional<User> user = userService.findUserByEmail(token.get().getUser().getEmail());
			user.get().setActive(true);
			userService.save(user.get());
			
			log.info("Check token for register successful");
			return "users/accountVerified";
		} else {
			log.error("Check token for register has error 'The link is invalid or broken'");
			model.addAttribute("message", "The link is invalid or broken!");
			return "users/verifyTokenError";
		}

	}
}
