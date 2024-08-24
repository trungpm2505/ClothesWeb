package com.web.clothes.ClothesWeb.dto.requestDto;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderRequestDto {
	
	@Size(min = 1, max = 70,message="Full Name cannot be empty and exceed 70 characters")
	private String fullName;
	
	@Pattern(regexp = "^(\\d{10}|\\d{12})$", message = "Phone can not empty and should be between 10 to 12 digits")
	private String phone;
	
	@Size(min=1, max = 100,message="Address must not exceed 100 characters")
	private String address;
	
	@Size(min=0, max = 500,message="Note must not exceed 500 characters")
	private String note;
	
	List<Integer> cartIds;

}
