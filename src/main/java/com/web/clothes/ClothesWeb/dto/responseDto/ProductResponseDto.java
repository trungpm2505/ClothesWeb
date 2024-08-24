package com.web.clothes.ClothesWeb.dto.responseDto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductResponseDto {

	private Integer id;

	private String title;
	
	private Float price;

	private Float currentPrice;
	
	private int quantity;
	
	private int quantityOfSold;

	private boolean gender;

	private String description;

	private LocalDate createAt ;

	private LocalDate updateAt;
	
	private String categoryName;
	
	private ProductAttributeValueResponseDto productAttributeValue;

	private List<ImageResponseDto> images;

//	public ProductResponseDto(Integer id, String title, String price, String currentPrice, boolean gender,
//			String description, LocalDate createAt, LocalDate updateAt, String categoryName,
//			List<ImageResponseDto> images) {
//		super();
//		this.id = id;
//		this.title = title;
//		this.price = price;
//		this.currentPrice = currentPrice;
//		this.gender = gender;
//		this.description = description;
//		this.createAt = createAt;
//		this.updateAt = updateAt;
//		this.categoryName = categoryName;
//		this.images = images;
//	}
	
	
}
