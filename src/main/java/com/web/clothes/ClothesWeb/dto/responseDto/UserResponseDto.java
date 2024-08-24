package com.web.clothes.ClothesWeb.dto.responseDto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponseDto {
	private Integer id;
	private String fullName;
    private String userName;
    private String email;
    private String phone;
    private String address;
    private String roleName;
	public UserResponseDto(String userName, String roleName) {
		super();
		this.userName = userName;
		this.roleName = roleName;
	}
    
    
}
