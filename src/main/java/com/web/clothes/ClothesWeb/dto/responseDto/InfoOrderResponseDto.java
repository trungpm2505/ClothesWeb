package com.web.clothes.ClothesWeb.dto.responseDto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InfoOrderResponseDto {
	private OrderResponseDto orderResponseDto;
	List<OrderDetailsResponseDto> orderDetailsResponseDtos;
}
