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
public class FeedbackRequestDto {
	private Integer id;
	@Size(min=1,max = 100,message="Email cannot be empty and must not exceed 100 characters")
	@Email(message="Email should be valid")
	private String email;
	
	@NotBlank(message="Full name cannot be empty")
	@Size(max = 70,message="Full name must not exceed 70 characters")
	private String fullName;
	
	@Pattern(regexp = "^\\+?[0-9]{10,12}$", message = "Phone can not empty and should be between 10 to 12 digits")
	private String phone;
	
	@NotBlank(message="Subject Name cannot be empty")
	private String subjectName;
	
	@NotBlank(message="Not cannot be empty and must not exceed 1500 characters")
	@Size(max = 1500,message="Not cannot be empty and must not exceed 1500 characters")
	private String note;
	
	private Integer userId;
}
