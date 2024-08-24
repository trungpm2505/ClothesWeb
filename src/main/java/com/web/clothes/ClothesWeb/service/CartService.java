package com.web.clothes.ClothesWeb.service;

import java.util.List;
import java.util.Optional;


import com.web.clothes.ClothesWeb.entity.Cart;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.User;

public interface CartService {
	public void save(Cart cart);
	public Optional<Cart> findByProductAndUser(Product product,User user);
	//public Page<Cart> findCartByUser(int pageNumber, int szie,User user);
	public List<Cart> findCartByUser(User user);
	public void updateQuantity(Cart cart);
	public Optional<Cart> getCart(Integer cartId);
	public void deleteCart(Integer Card);
}
