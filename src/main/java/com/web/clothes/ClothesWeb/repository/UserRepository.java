package com.web.clothes.ClothesWeb.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
	public Optional<User> findUserByUserName(String userName);
	public Optional<User> findUserByEmail(String email);
	public Optional<User> findUserByPhone(String phone);
	
	public Optional<User> getUserById(Integer userId);
	
	@Query("SELECT c FROM User c WHERE c.deleteAt is null")
    Page<User> findAll(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.deleteAt is null AND u.fullName LIKE %:keyword%")
	 Page<User> findByKeyword(Pageable pageable,String keyword);
}
