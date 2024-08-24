package com.web.clothes.ClothesWeb.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MailInfoDto {
	String to;
	String subject;
	String body;

}
