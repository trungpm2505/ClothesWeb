package com.web.clothes.ClothesWeb.dto.requestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryRequestDto {
	@NotBlank(message="Category name cannot be empty and must not exceed 30 characters")
	@Size(min = 1, max = 30,message="Category name cannot be empty and must not exceed 30 characters")
	private String categoryName;
}
