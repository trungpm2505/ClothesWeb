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
public class OrderResponseDto {

	private Integer id;
	private String fullName;
	private String phone;
	private String address;
	private LocalDate createAt ;
	private LocalDate completedAt;
	private float totalMoney;
	private int status;
	private String note;
	private String userName;
	private boolean rated;
	List<OrderDetailsResponseDto> orderDetaitsResponseDtos;
	public OrderResponseDto(Integer id, String fullName, String phone, String address, LocalDate createAt,
			float totalMoney, int status, String note,String userName,boolean rate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.address = address;
		this.createAt = createAt;
		this.totalMoney = totalMoney;
		this.status = status;
		this.note = note;
		this.userName = userName;
		this.rated = rate;
	}
	
	public OrderResponseDto(Integer id, String fullName, String phone, String address, LocalDate createAt,LocalDate completedAt,
			float totalMoney, int status, String note,String userName) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.address = address;
		this.createAt = createAt;
		this.completedAt= completedAt;
		this.totalMoney = totalMoney;
		this.status = status;
		this.note = note;
		this.userName = userName;
		
	}
	
	

}
