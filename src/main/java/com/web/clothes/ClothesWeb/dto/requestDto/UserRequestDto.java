package com.web.clothes.ClothesWeb.dto.requestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRequestDto {
//	@NotBlank(message="Username cannot be empty")
	@Size(min = 4, max = 50,message="Username cannot be empty and must be between 4 and 50 characters long")
	private String username;
	
	@Size(min = 6, max = 30,message="Password cannot be empty and must be between 6 and 30 characters long")
	private String password;
	
	
	private String confirmPassword;
	
	@NotBlank(message="Fullname cannot be empty")
	@Size(max = 70,message="Full name must not exceed 70 characters")
	private String fullname;
	
	@NotBlank(message="Address cannot be empty")
	@Size( max = 100,message="Address must not exceed 100 characters")
	private String address;
	
	@NotBlank(message="Phone cannot be empty")
	@Pattern(regexp = "^\\+?[0-9]{10,12}$", message = "Phone can not empty and should be between 10 to 12 digits")
	private String phone;
	
	
	@Size(min=1,max = 100,message="Email cannot be empty and must not exceed 100 characters")
	@Email(message="Email should be valid")
	private String email;
	
}
