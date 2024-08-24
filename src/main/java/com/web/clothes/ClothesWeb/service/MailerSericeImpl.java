package com.web.clothes.ClothesWeb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.dto.requestDto.MailInfoDto;
import com.web.clothes.ClothesWeb.entity.ConfirmationToken;
import com.web.clothes.ClothesWeb.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailerSericeImpl implements MailerService {
	
	private final JavaMailSender javaMailSender;
	private final ConfirmationTokenService confirmationTokenService;

	List<MailInfoDto> list = new ArrayList<>();

	@Override
	public void send(MailInfoDto mail) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(mail.getTo());
		msg.setSubject(mail.getSubject());
		msg.setText(mail.getBody());

		javaMailSender.send(msg);
	}
	Random random = new Random();
	@Override
	public void sendEmailToConfirmAccount(User user) {
		ConfirmationToken confirmationToken = new ConfirmationToken(UUID.randomUUID().toString(),user,LocalDateTime.now().plusMinutes(2));
	
		confirmationTokenService.save(confirmationToken);
		MailInfoDto mailInfoDto = new MailInfoDto(user.getEmail(), "Puu-Verify Your Account",
				" Thank you for signing up for our service. To ensure the security of your account, please verify your email address by clicking on the link below:"
						+ "http://localhost:8080/register/confirm-account?token=" + confirmationToken.getToken());
		
		send(mailInfoDto);
		
	}
	
	@Override
	public void sendEmailToResetPassword(User user) {
		ConfirmationToken confirmationToken = new ConfirmationToken(String.valueOf(random.nextInt(900000) + 100000),user,LocalDateTime.now().plusMinutes(2));
		confirmationTokenService.save(confirmationToken);
		MailInfoDto mailInfoDto = new MailInfoDto(user.getEmail(), "Puu-Reset Your Password",
				"Dear "+user.getUserName()+",\r\n"
				+ "\r\n"
				+ "You have requested to reset the password for your account. Below is the confirmation code to complete the password reset process:\r\n"
				+ "\r\n"
				+ "Confirmation Code:" + confirmationToken.getToken()
				+ "\r\n"
				+"Please enter this confirmation code on the password reset page to proceed with the process.\r\n"
				+ "\r\n"
				+ "If you did not request a password reset, please disregard this email. Your account will not be affected.\r\n"
				+ "\r\n"
				+ "Thank you for your attention.\r\n"
				+ "\r\n"
				+ "Sincerely,\r\n"
				+ "Puu Shop"
				)
				
				;
		
		send(mailInfoDto);
		
	}

}
