package com.web.clothes.ClothesWeb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Cart;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.User;
@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
	public Optional<Cart> findCartByProductAndUser(Product product,User user);
	//public Page<Cart> findCartByUser(Pageable pageable,User user);
	public List<Cart> findCartByUser(User user);
}
