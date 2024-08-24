package com.web.clothes.ClothesWeb.dto.requestDto;

import java.time.LocalDate;


import javax.validation.constraints.NotNull;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartRequestDto {
	
	private int number;
	
    private LocalDate createAt = LocalDate.now();
    
    private LocalDate updateAt;
    
    private LocalDate deleteAt;
    @NotNull()
	private Integer productId;
	
    private Integer userId;
}
