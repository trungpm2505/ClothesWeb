package com.web.clothes.ClothesWeb.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.dto.requestDto.ChangePasswordRequestDto;
import com.web.clothes.ClothesWeb.dto.requestDto.LoginRequestDto;
import com.web.clothes.ClothesWeb.dto.requestDto.UserRequestDto;
import com.web.clothes.ClothesWeb.dto.responseDto.AuthenticationResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.RoleResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.UserPageResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.UserResponseDto;
import com.web.clothes.ClothesWeb.entity.ConfirmationToken;
import com.web.clothes.ClothesWeb.entity.Role;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.jwt.CustomUserDetails;
import com.web.clothes.ClothesWeb.jwt.JwtTokenProvider;
import com.web.clothes.ClothesWeb.repository.UserRepository;
import com.web.clothes.ClothesWeb.service.ConfirmationTokenService;
import com.web.clothes.ClothesWeb.service.MailerService;
import com.web.clothes.ClothesWeb.service.RoleService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
	private static Log log = LogFactory.getLog(UserController.class);
	
	private final UserService userService;

	private final RoleService roleService;

	private final Mapper mapper;

	

	@PutMapping(value = "/update")
	@ResponseBody
	public ResponseEntity<?> updateUser(@RequestParam("userId") Integer userId,
			@RequestParam("roleId") Integer roleID) {
		log.info("Updating user with userId: " + userId);
		Map<String, Object> errors = new HashMap<>();
	
		// check if Attribute value is exist
		Optional<User> userById = userService.getUsers(userId);
		Optional<Role> role = roleService.getRole(roleID);
		if (userById.isEmpty()) {
			errors.put("error", "User is not exist! Update failse!");
			log.error("User with userId "+userId+" does not exist. Update failed." );
			return ResponseEntity.badRequest().body(errors);
		}

		userById.get().setRole(role.get());
		userService.save(userById.get());
		log.info("User with userId "+userId+" updated successfully." );
		return ResponseEntity.ok().body("A User update successfully.");
	}

	@DeleteMapping(value = "/delete")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> deleteUser(@RequestParam("userId") Integer userId) {
		log.info("Deleting user with userId: " + userId);
		
		// check if user is exist
		Optional<User> userById = userService.getUsers(userId);
		if (userById.isEmpty()) {
			log.error("User with userId "+userId+" does not exist. Delete failed." );
			return ResponseEntity.badRequest().body("User is not exist! Delete failse!");
		}
		userService.deleteUser(userById.get());
		log.info("User with userId "+userId+" deleted successfully." );
		return ResponseEntity.ok().body("A User deleted successfully.");
	}

	// return view user
	@GetMapping(value = "/list")
	public String getCategoryView(Model model) {
		log.info("view user list in admin page");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		return "admin/user";
	}

	// get page user
	@GetMapping(value = "/getUserPage")
	@ResponseBody
	public ResponseEntity<UserPageResponseDto> getUserPage(@RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword) {
		log.info("get user page value");
		
		Page<User> userPage = null;
		if (keyword != null) {
			userPage = userService.getUserByKey(page, size, keyword);
		} else {
			userPage = userService.getAllUser(page, size);
		}

		List<UserResponseDto> userResponseDtos = userPage
				.stream().map(user -> new UserResponseDto(user.getId(), user.getFullName(), user.getUserName(),
						user.getEmail(), user.getPhone(), user.getAddress(), user.getRole().getRoleName()))
				.collect(Collectors.toList());

		UserPageResponseDto userPageResponseDto = new UserPageResponseDto(userPage.getTotalPages(),
				userPage.getNumber(), userPage.getSize(), userResponseDtos);

		return ResponseEntity.ok(userPageResponseDto);

	}

	// return view role
	@GetMapping(value = "/getAllRole")
	public ResponseEntity<?> getAllRole(String a) {
		log.info("get role list");
		
		List<Role> roles = roleService.getAll();

		List<RoleResponseDto> roleResponseDtos = roles.stream()
				.map(role -> new RoleResponseDto(role.getId(), role.getRoleName())).collect(Collectors.toList());

		if (roleResponseDtos.isEmpty()) {
			return ResponseEntity.badRequest().body("list rỗng");
		}
		return ResponseEntity.ok(roleResponseDtos);
	}

	@GetMapping("/accountUser")
	public String accountUser(Model model) {
		log.info("get account view in user page");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {

			String currentPrincipalName = authentication.getName();
			Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
			model.addAttribute("userName",userByEmail.get().getUserName());
			// validate input data

			if (userByEmail.isPresent()) {
				UserRequestDto userRequestDto = new UserRequestDto();
				userRequestDto.setUsername(userByEmail.get().getUserName());
				userRequestDto.setFullname(userByEmail.get().getFullName());
				userRequestDto.setEmail(userByEmail.get().getEmail());
				userRequestDto.setPhone(userByEmail.get().getPhone());
				userRequestDto.setAddress(userByEmail.get().getAddress());

				model.addAttribute("userRequestDto", userRequestDto);
			}
		} else {
			model.addAttribute("userRequestDto", new UserRequestDto());
		}
		return "users/account_user";
	}

	@GetMapping("/accountAdmin")
	public String accountAdmin(Model model) {
		log.info("get account view in admin page");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("userName", userService.findUserByEmail(authentication.getName()).get().getUserName());
		if (authentication != null) {

			String currentPrincipalName = authentication.getName();
			Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
			// validate input data

			if (userByEmail.isPresent()) {
				UserRequestDto userRequestDto = new UserRequestDto();
				userRequestDto.setUsername(userByEmail.get().getUserName());
				userRequestDto.setFullname(userByEmail.get().getFullName());
				userRequestDto.setEmail(userByEmail.get().getEmail());
				userRequestDto.setPhone(userByEmail.get().getPhone());
				userRequestDto.setAddress(userByEmail.get().getAddress());

				model.addAttribute("userRequestDto", userRequestDto);
			}
		}
		return "admin/updateAccount";
	}

	@PostMapping("/updateAccount")
	@ResponseBody
	public ResponseEntity<Map<String, String>> updateAccount(
			@Valid @ModelAttribute("userRequestDto") UserRequestDto userRequestDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Optional<User> userByEmail = userService.findUserByEmail(currentPrincipalName);
		
		log.info("Update account with userId "+ userByEmail.get().getId());
		
		Map<String, String> errors = new HashMap<>();

		if (bindingResult.hasErrors()) {
			log.error("Update with userId "+userByEmail.get().getId()+" have error "+bindingResult.getFieldErrors() );
			
			// Xử lý lỗi binding và tạo danh sách thông báo lỗi
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errors);
		}

		
		Map<String, String> successMessage = new HashMap<>();
		if (userByEmail.isPresent()) {
			User user = userByEmail.get();

			user.setUserName(userRequestDto.getUsername());
			user.setFullName(userRequestDto.getFullname());
			user.setPhone(userRequestDto.getPhone());
			user.setAddress(userRequestDto.getAddress());
			user.setUpdateAt(LocalDate.now());

			userService.save(user);

			// Trả về thông báo thành công
			successMessage.put("successMessage", "Account updated successfully!");
		}
		log.info("Update account with userId "+ userByEmail.get().getId()+" successful.");
		return ResponseEntity.ok(successMessage);
	}

}
