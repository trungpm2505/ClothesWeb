package com.web.clothes.ClothesWeb.service;


import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.clothes.ClothesWeb.entity.User;

public interface UserService {
	public User getUser(Integer userId);
	public Optional<User> getUsers(Integer userId);
	public void save(User user);
	public void deleteUser(User user);
	public Optional<User> findUserByEmail(String email );
	public Optional<User> findUserByPhone(String phone );
	public Optional<User> findUserByUsername(String username );
	
	public Page<User> getAllUser(int pageNumber, int szie);
	public Page<User> getUserByKey(int pageNumber, int size,String keyWord);
	
}
