package com.web.clothes.ClothesWeb.service;


import java.util.Optional;

import com.web.clothes.ClothesWeb.entity.ConfirmationToken;
import com.web.clothes.ClothesWeb.entity.User;


public  interface ConfirmationTokenService {
	public ConfirmationToken save(ConfirmationToken ConfirmationToken);
	public Optional<ConfirmationToken> getConfirmationTokenByToken(String token);
	public Optional<ConfirmationToken> getConfirmationTokenByTokenAndUser(String token,User user);
}
