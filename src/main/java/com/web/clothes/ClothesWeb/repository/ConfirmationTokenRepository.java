package com.web.clothes.ClothesWeb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.ConfirmationToken;
import com.web.clothes.ClothesWeb.entity.User;
@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Integer>{
	Optional<ConfirmationToken> findByToken(String token);
	Optional<ConfirmationToken> findByTokenAndUser(String token,User user);
	
}
