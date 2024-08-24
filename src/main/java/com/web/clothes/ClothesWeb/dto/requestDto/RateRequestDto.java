package com.web.clothes.ClothesWeb.dto.requestDto;

import javax.validation.constraints.Min;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RateRequestDto {
	private Integer rateId;
	@Min(value=1, message="Product cannot be empty.")
	private Integer productId;
	@Min(value=1, message="Rate cannot be empty.")
	private int rating;
	private Integer orderId;
    private String content;
}
