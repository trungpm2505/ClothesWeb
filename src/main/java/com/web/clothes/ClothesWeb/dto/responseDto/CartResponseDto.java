package com.web.clothes.ClothesWeb.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartResponseDto {
	private Integer cartId;
	
	private int quantity;
	
	private ProductResponseDto productResponseDto;
	
	private Float price;

	private Float currentPrice;
	
	private Float total;
	
	private ProductAttributeValueResponseDto productAttributeValue;

	private ImageResponseDto images;
}
