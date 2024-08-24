package com.web.clothes.ClothesWeb.service;

import com.web.clothes.ClothesWeb.dto.requestDto.MailInfoDto;
import com.web.clothes.ClothesWeb.entity.User;

public interface MailerService {
	void send(MailInfoDto mail);
	void sendEmailToConfirmAccount(User users);
	void sendEmailToResetPassword(User users);
	

}
