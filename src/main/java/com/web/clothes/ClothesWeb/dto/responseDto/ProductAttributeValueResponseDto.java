package com.web.clothes.ClothesWeb.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductAttributeValueResponseDto {
	private Integer id;
	private Integer sizeId;
	private String size;
	private Integer colorId;
	private String color;
}
