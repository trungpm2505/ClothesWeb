package com.web.clothes.ClothesWeb.dto.requestDto;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductRequestDto {
	private Integer id;
	@NotBlank(message="Title cannot be empty and must not exceed 50 characters.")
	@Size(min = 1, max = 300,message="Title cannot be empty and must not exceed 300 characters.")
	private String title;
	
    @Min(value=0, message="Price cannot be empty and must be a positive number.")
	private float price;
    
    @Min(value=0, message="Curent price must be a positive number.")
	private float currentPrice;
    
    @Min(value=0, message="Quantity must be a positive number.")
	private int quantity;
    
    @NotBlank(message = "Please choose gender.")
	private String gender;
	
    @NotBlank(message="Discription cannot be empty and must not exceed 1500 characters")
	@Size(min = 1, max = 1500,message="Discription cannot be empty and must not exceed 1500 characters")
	private String description;
	
	private Integer sizeAttributeValue;

	private Integer colorAttributeValue;
	
	@NotNull(message="Category cannot be empty.")
	private Integer category;
	
}
