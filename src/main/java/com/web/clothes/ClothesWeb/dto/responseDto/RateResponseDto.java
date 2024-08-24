package com.web.clothes.ClothesWeb.dto.responseDto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RateResponseDto {
	
    private Integer id;
    
	private LocalDateTime createAt;
    
    private int rating;
    
    private String content;
    
    private UserResponseDto userResponseDto;
    
    private List<ResponseResponseDto> responses;
    
    private List<ImageResponseDto> images;
}
