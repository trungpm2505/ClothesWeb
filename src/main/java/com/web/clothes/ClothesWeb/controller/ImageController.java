package com.web.clothes.ClothesWeb.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.web.clothes.ClothesWeb.dto.mapper.Mapper;
import com.web.clothes.ClothesWeb.jwt.JwtTokenProvider;
import com.web.clothes.ClothesWeb.repository.UserRepository;
import com.web.clothes.ClothesWeb.service.ConfirmationTokenService;
import com.web.clothes.ClothesWeb.service.ImageService;
import com.web.clothes.ClothesWeb.service.MailerService;
import com.web.clothes.ClothesWeb.service.RoleService;
import com.web.clothes.ClothesWeb.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ImageController {
	private final ImageService imageService;
	
}
