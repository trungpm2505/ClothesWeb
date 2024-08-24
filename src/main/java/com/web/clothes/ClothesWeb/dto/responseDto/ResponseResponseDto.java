package com.web.clothes.ClothesWeb.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseResponseDto {
	private Integer id;
	
	private UserResponseDto userResponseDto;
    
    private String content;
}
