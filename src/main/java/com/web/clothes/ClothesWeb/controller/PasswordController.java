package com.web.clothes.ClothesWeb.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.requestDto.ChangePasswordRequestDto;
import com.web.clothes.ClothesWeb.dto.requestDto.CheckTokenResetPass;
import com.web.clothes.ClothesWeb.dto.requestDto.EmailRequestDto;
import com.web.clothes.ClothesWeb.dto.requestDto.ResetPassRequestDto;
import com.web.clothes.ClothesWeb.entity.ConfirmationToken;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.service.ConfirmationTokenService;
import com.web.clothes.ClothesWeb.service.MailerService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/password")
public class PasswordController {
	private final UserService userService;
	private final MailerService mailerService;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationTokenService confirmationTokenService;
	private static Log log = LogFactory.getLog(PasswordController.class);
	
	@GetMapping("/change")
	public String changePassWord(HttpSession session,Model model) {
		log.info("Get change passWord view for user"+(String) session.getAttribute("userName"));
		
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		
		return "users/changePassword";
	}
	@GetMapping("/change/admin")
	public String changePassWordForAdmin(HttpSession session,Model model) {
		log.info("Get change passWord view for user"+(String) session.getAttribute("userName"));
		
		model.addAttribute("userName",(String) session.getAttribute("userName"));
		return "admin/changePassword";
	}
	
	@PutMapping("/checkChange")
	@ResponseBody
	public ResponseEntity<?> checkChangePassword( @Valid @ModelAttribute ChangePasswordRequestDto changePasswordRequestDto,
			 BindingResult bindingResult) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentPrincipalName = authentication.getName();
	    Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
	    
	    log.info("Check change passWord for user "+userByEmail.get().getId());
	    
	    Map<String, Object> errors = new HashMap<>();

	    if (bindingResult.hasErrors()) {
	    	log.error("Check change passWord for user "+userByEmail.get().getId()+" has error "+bindingResult.getAllErrors());
	    	
	        // Xử lý lỗi binding và tạo danh sách thông báo lỗi
	            errors.put("bindingErrors",bindingResult.getAllErrors());
	    }
	 
	    	 if(!bindingResult.getFieldErrors().stream()
	    		        .anyMatch(error -> error.getField().equals("currentPassword"))) {
	    		 if(!passwordEncoder.matches(changePasswordRequestDto.getCurrentPassword(), userByEmail.get().getPassword())){
	    			 log.error("Check change passWord for user "+userByEmail.get().getId()+" has error 'wrong current password'");
	    			 errors.put("currentPassword", "wrong current password.");
	    			
	    		 }
	 	    }
	 		
	 		if(!bindingResult.getFieldErrors().stream()
    		        .anyMatch(error -> error.getField().equals("confirmNewPassword"))) {
	 			if(!changePasswordRequestDto.getNewPassword().equals(changePasswordRequestDto.getConfirmNewPassword())) {
	 				log.error("Check change passWord for user "+userByEmail.get().getId()+" has error 'New password not match'");
	 				errors.put("confirmNewPassword", "New password not match.");
	 				 
	 			}
	 		}
		
	   
		
		if(!errors.isEmpty()) {
			
			 return ResponseEntity.badRequest().body(errors);
		}
	    
		String encodedPassword = passwordEncoder.encode(changePasswordRequestDto.getConfirmNewPassword());
		userByEmail.get().setPassword(encodedPassword);
	    
	    userService.save(userByEmail.get());
	    
	    log.info("Check change passWord for user "+userByEmail.get().getId()+" successful");
	    
	    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
	            .body("{\"message\": \"Ok\"}");
	}
	
	@GetMapping("/reset")
	public String changePassWord(Model model) {
		log.info("Get reset passWord view");
		return "users/resetPassword";
	}
	
	@PostMapping("/sentEmail")
	public ResponseEntity<String> sentEmailToResetPassword(@Valid @RequestBody EmailRequestDto email,BindingResult bindingResult) {
		log.info("Send email for reset passWord to "+email);
		if (bindingResult.hasErrors()) {
			
			log.error("Send email for reset passWord to "+email+" has error 'invalid email'");
			
			return ResponseEntity.badRequest().body("{\"message\": \"Please enter valid email.\"}");
	           
	    }
		Optional<User> userByEmail = userService.findUserByEmail(email.getEmail());
		if(userByEmail.isEmpty()) {
			
			log.error("Send email for reset passWord to "+email+" has error 'No account exists with the email you entered.'");
			
			 return ResponseEntity.badRequest().body("{\"message\": \"No account exists with the email you entered.\"}");
		}
		
		if(!userByEmail.get().isActive()) {
			log.error("Send email for reset passWord to "+email+" has error 'The account has not been verified, please check your email to verify'.");
			return ResponseEntity.badRequest().body("{\"message\": \"The account has not been verified, please check your email to verify.\"}");
		}
		
		mailerService.sendEmailToResetPassword(userByEmail.get());
		
		log.info("Send email for reset passWord to "+email+" successful");
		 return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
		            .body("{\"message\": \"Ok\"}");
	}

	@GetMapping(value="/getTokenReset")
	public String getTokenReset() {
		log.info("Get get token view");
		return "users/getTokenResetPassword";
	}
	
	@PostMapping("/checkTokenReset")
	@ResponseBody
	public ResponseEntity<String> checkTokenReset(@Valid @RequestBody CheckTokenResetPass checkTokenResetPass,BindingResult bindingResult) {
		log.info("Check token reset pass");
		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body("{\"message\": \"Code has 6 digits.\"}");
	    }
		Optional<User> user =userService.findUserByEmail(checkTokenResetPass.getEmail());
		Optional<ConfirmationToken> token = confirmationTokenService.getConfirmationTokenByTokenAndUser(checkTokenResetPass.getToken(),user.get());
		
		// if token is not valid
		if (!token.isPresent()) {
			log.error("Check token reset pass has error 'The link is invalid'");
			return ResponseEntity.badRequest().body("{\"message\": \"The link is invalid!.\"}");
		}
		// if token is valid but expiryDate

		if (token.isPresent()) {

			if (token.get().getExpiryDate().isBefore(LocalDateTime.now())) {
				log.error("Check token reset pass has error 'The link is broken.Click resent to get code again'");
				return ResponseEntity.badRequest().body("{\"message\": \"The link is broken.Click resent to get code again.!\"}");
			}
			log.info("Check token reset pass successful");
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
		            .body("{\"message\": \"Ok\"}");
		} else {
			return ResponseEntity.badRequest().body("{\"message\": \"The link is invalid!\"}");
		}

	}
	@GetMapping(value="/setNewPassView")
	public String setNewPassView() {
		log.info("Get set new password view");
		return "users/setNewPassForReset";
	}
	
	@PutMapping("/checkResetPass")
	@ResponseBody
	public ResponseEntity<?> setNewPass( @Valid @ModelAttribute ResetPassRequestDto resetPassRequestDto,
			 BindingResult bindingResult) {
		log.info("Check reset password");
		Map<String, Object> errors = new HashMap<>();
	    if (bindingResult.hasErrors()) {
	    	log.error("Check reset password has error "+bindingResult.getAllErrors());
	    	
	        // Xử lý lỗi binding và tạo danh sách thông báo lỗi
	    	errors.put("bindingErrors",bindingResult.getAllErrors());
	    	
	    }
	 		
	 		if(!bindingResult.getFieldErrors().stream()
    		        .anyMatch(error -> error.getField().equals("newPass"))) {
	 			if(!resetPassRequestDto.getNewPass().equals(resetPassRequestDto.getNewPassAgain())) {
	 				log.error("Check reset password has error 'New password not match'");
	 				errors.put("message","New password not match.");
	 				 
	 			}
	 		}
	 		if(!errors.isEmpty()) {
				
				 return ResponseEntity.badRequest().body(errors);
			}
		String encodedPassword = passwordEncoder.encode(resetPassRequestDto.getNewPass());
		Optional<User> userByEmail = userService.findUserByEmail(resetPassRequestDto.getEmail());
		userByEmail.get().setPassword(encodedPassword);
	    
	    userService.save(userByEmail.get());
	    
	    log.info("Check reset password successful");
	    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
	            .body("{\"message\": \"Ok\"}");
	}
	
	@GetMapping(value="/resetPassSuccess")
	public String resetPassSuccess() {
		log.info("Get resetPassSuccess view");
		return "users/resetPassSuccess";
	}
}
