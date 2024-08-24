package com.web.clothes.ClothesWeb.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImageResponseDto {
	private Integer id;

	private String inmageForShow;
	
	private String inmageForSave;
	
	private Boolean isDefault ;

}
