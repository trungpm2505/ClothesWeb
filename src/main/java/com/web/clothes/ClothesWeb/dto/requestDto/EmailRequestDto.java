package com.web.clothes.ClothesWeb.dto.requestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmailRequestDto {
	@Size(min=1,max = 100,message="Email cannot be empty and must not exceed 100 characters")
	@Email(message="Email should be valid")
	private String email;
}
