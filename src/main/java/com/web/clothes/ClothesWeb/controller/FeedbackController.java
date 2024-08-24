package com.web.clothes.ClothesWeb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.security.core.Authentication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.FeedbackRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.FeedbackPageResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.FeedbackResponseDto;
import com.web.clothes.ClothesWeb.entity.Feedback;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.service.FeedbackService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/feedback")
public class FeedbackController {
	@Autowired
	private FeedbackService feedbackService;

	private final UserService userService;

	private final Mapper mapper;
	
	private static Log log = LogFactory.getLog(FeedbackController.class);

	@GetMapping("/admin/list")
	public String feedbackViewAdmin(Model model) {
		log.info("Get feedback view in admin page");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());

		return "admin/feedback";
	}

//	@GetMapping("/user/form")
//	public String feedbackViewUser(HttpSession session, Model model) {
//		model.addAttribute("userName", (String) session.getAttribute("userName"));
//
//		String currentPrincipalName = (String) session.getAttribute("email");
//		Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
//		// validate input data
//
//		if (userByEmail.isPresent()) {
//			FeedbackRequestDto feedbackRequestDto = new FeedbackRequestDto();
//			feedbackRequestDto.setEmail(userByEmail.get().getEmail());
//			feedbackRequestDto.setFullName(userByEmail.get().getFullName());
//			feedbackRequestDto.setPhone(userByEmail.get().getPhone());
//			feedbackRequestDto.setSubjectName("");
//			feedbackRequestDto.setNote("");
//
//			model.addAttribute("feedbackRequestDto", feedbackRequestDto);
//		} else {
//			model.addAttribute("feedbackRequestDto", new FeedbackRequestDto());
//		}
//
//		return "users/feedback";
//	}

	@GetMapping("/user/form")
	public String feedbackViewUser(HttpSession session, Model model) {
		log.info("Get feedback view in user page");
	    model.addAttribute("userName", (String) session.getAttribute("userName"));

	    String currentPrincipalName = (String) session.getAttribute("email");
	    Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
	    // validate input data

	    FeedbackRequestDto feedbackRequestDto;
	    if (userByEmail.isPresent()) {
	        feedbackRequestDto = new FeedbackRequestDto();
	        feedbackRequestDto.setEmail(userByEmail.get().getEmail());
	        feedbackRequestDto.setFullName(userByEmail.get().getFullName());
	        feedbackRequestDto.setPhone(userByEmail.get().getPhone());
	        feedbackRequestDto.setSubjectName("");
	        feedbackRequestDto.setNote("");
	        
	    } else {
	        feedbackRequestDto = new FeedbackRequestDto();
	    }

	    model.addAttribute("feedbackRequestDto", feedbackRequestDto);

	    return "users/feedback";
	}

	@PostMapping(value = "/user/add")
	@ResponseBody
	public ResponseEntity<Map<String, String>> addFeedback(HttpSession session,
	        @Valid @RequestBody FeedbackRequestDto feedbackRequestDto, BindingResult bindingResult) {
		
		log.info("Add feedback with email: " + feedbackRequestDto.getEmail());
		
	    if (bindingResult.hasErrors()) {
	    	
	    	log.error("Add feedback with email: " + feedbackRequestDto.getEmail()+" has error "+ bindingResult.getAllErrors());
	    	
	        Map<String, String> errors = new HashMap<>();
	        for (FieldError error : bindingResult.getFieldErrors()) {
	            errors.put(error.getField(), error.getDefaultMessage());
	        }
	        return ResponseEntity.badRequest().body(errors);
	    }

	    Feedback feedback = mapper.feebackRequestDtoToFeedback(feedbackRequestDto);

	    String currentPrincipalName = (String) session.getAttribute("email");
	    Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
	    if (userByEmail.isPresent()) {
	    	log.error("Add feedback with user: " + userByEmail.get().getId());
	        feedback.setUser(userByEmail.get());
	    }

	    feedbackService.save(feedback);

	    Map<String, String> response = new HashMap<>();
	    response.put("successMessage", "Feedback successfully!");

	    log.info("Add feedback with email: " + feedbackRequestDto.getEmail()+" successful");
	    return ResponseEntity.ok().body(response);
	}



	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteFeedback(@RequestParam("feedbackId") Integer feedbackId) {

		// check if feedback is exist
		Optional<Feedback> feedbackById = feedbackService.getFeedback(feedbackId);
		if (feedbackById.isEmpty()) {
			return ResponseEntity.badRequest().body("Feedback is not exist! Delete failse!");
		}

		feedbackService.deleteFeedback(feedbackById.get());
		return ResponseEntity.ok().body("A feedback deleted successfully.");
	}

	@GetMapping(value = "/viewDetail")
	@ResponseBody
	public ResponseEntity<FeedbackResponseDto> viewDetail(@RequestParam("feedbackId") Integer feedbackId) {
		log.info("get feedback details view with feedback: " + feedbackId);
		
		Optional<Feedback> feedbackById = feedbackService.getFeedback(feedbackId);
		if (feedbackById.isPresent()) {
			Feedback feedback1 = feedbackById.get();
			// Logic xử lý khi feedback tồn tại
			List<FeedbackResponseDto> feedbackResponseDtos = Stream.of(feedback1)
					.map(feedback -> new FeedbackResponseDto(feedback.getId(), feedback.getEmail(),
							feedback.getFullName(), feedback.getPhone(), feedback.getSubjectName(), feedback.getNote(),
							feedback.getCreateAt()))
					.collect(Collectors.toList());
			return ResponseEntity.ok(feedbackResponseDtos.get(0));
		} else {
			// Logic xử lý khi feedback không tồn tại
			return ResponseEntity.notFound().build();
		}
	}

	// get page category
	@GetMapping(value = "/getFeedbackPage")
	@ResponseBody
	public ResponseEntity<FeedbackPageResponseDto> getFeedbackPage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
		log.info("get feedback page value");
		
		Page<Feedback> feedbackPage = null;
		if (keyword != null) {
			feedbackPage = feedbackService.getFeedbackByKey(page, size, keyword);
		} else {
			feedbackPage = feedbackService.getAllFeedback(page, size);
		}

		List<FeedbackResponseDto> feedbackResponseDtos = feedbackPage.stream()
				.map(feedback -> new FeedbackResponseDto(feedback.getId(), feedback.getEmail(), feedback.getFullName(),
						feedback.getPhone(), feedback.getSubjectName(), feedback.getNote(), feedback.getCreateAt()))
				.collect(Collectors.toList());

		FeedbackPageResponseDto feedbackPageResponseDto = new FeedbackPageResponseDto(feedbackPage.getTotalPages(),
				feedbackPage.getNumber(), feedbackPage.getSize(), feedbackResponseDtos);

		log.info("get feedback page value successful");
		return ResponseEntity.ok(feedbackPageResponseDto);

	}
	@GetMapping("/user/contact")
	public String contactView(Model model,HttpSession session) {
		log.info("Get feedback view in admin page");
		
		model.addAttribute("userName",(String) session.getAttribute("userName"));

		return "users/contact";
	}

}
