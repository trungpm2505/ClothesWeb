package com.web.clothes.ClothesWeb.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.exception.UserNotFoundException;
import com.web.clothes.ClothesWeb.jwt.CustomUserDetails;

import com.web.clothes.ClothesWeb.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public User getUser(Integer userId) {
		// check role is exist
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new UserNotFoundException(HttpStatus.NOT_FOUND.value(), "Can not find user with id " + userId);
		}
		return user.get();
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findUserByEmail(email);
		CustomUserDetails c = new CustomUserDetails(user.get());
		System.out.println(c.getAuthorities());
		return c;
	}

	@Override
	public Optional<User> findUserByEmail(String email) {
		Optional<User> user = userRepository.findUserByEmail(email);
		return user;
	}

	@Override
	public Optional<User> findUserByPhone(String phone) {
		Optional<User> user = userRepository.findUserByPhone(phone);
		return user;
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		Optional<User> user = userRepository.findUserByUserName(username);
		return user;
	}

	@Override
	public Optional<User> getUsers(Integer userId) {
		Optional<User> user = userRepository.getUserById(userId);
		return user;
	}

	@Override
	public void deleteUser(User user) {
		//user.setDeleted(1);
		user.setDeleteAt(LocalDate.now());
		userRepository.save(user);
	}

	@Override
	public Page<User> getAllUser(int pageNumber, int szie) {

		PageRequest userPageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "userName"));

		Page<User> UserPage = userRepository.findAll(userPageable);

		return UserPage;
	}

	@Override
	public Page<User> getUserByKey(int pageNumber, int size, String keyWord) {
		PageRequest userPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "userName"));
		
		Page<User> UserPage = userRepository.findByKeyword(userPageable,keyWord);
		return UserPage;
	}

}
