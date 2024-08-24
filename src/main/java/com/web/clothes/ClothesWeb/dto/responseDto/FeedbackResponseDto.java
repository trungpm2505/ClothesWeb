package com.web.clothes.ClothesWeb.dto.responseDto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FeedbackResponseDto {
	private Integer id;
	private String email;
	private String fullName;
	private String phone;
	private String SubjectName;
	private String note;
	private LocalDate createAt;
	private Integer userId;
	
	public FeedbackResponseDto(Integer id, String email, String fullName, String phone, String subjectName, String note,
			LocalDate createAt) {
		this.id = id;
		this.email = email;
		this.fullName = fullName;
		this.phone = phone;
		SubjectName = subjectName;
		this.note = note;
		this.createAt = createAt;
	}
	
	
}
