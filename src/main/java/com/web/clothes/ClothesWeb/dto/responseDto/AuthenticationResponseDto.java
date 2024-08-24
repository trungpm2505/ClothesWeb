package com.web.clothes.ClothesWeb.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AuthenticationResponseDto {
	private String token;
	
	private String role;

	private String message;

}
