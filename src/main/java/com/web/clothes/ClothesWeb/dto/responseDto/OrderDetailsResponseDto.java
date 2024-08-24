
package com.web.clothes.ClothesWeb.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailsResponseDto {
	
	private float price;
	
	private float curentPrice;

	private int number;
	
	private float totalMoney;
	
	//private String productName;
	
	private ProductResponseDto productResponseDto;
	
	private String imageForSave;
	
	private ProductAttributeValueResponseDto productAttributeValue;

	public OrderDetailsResponseDto(float price, int number, float totalMoney, ProductResponseDto productName) {
		super();
		this.price = price;
		this.number = number;
		this.totalMoney = totalMoney;
		this.productResponseDto = productName;
	}
	
	
	
}
