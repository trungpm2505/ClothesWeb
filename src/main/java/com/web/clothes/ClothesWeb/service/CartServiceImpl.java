package com.web.clothes.ClothesWeb.service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Cart;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.User;
import com.web.clothes.ClothesWeb.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
	
	private final CartRepository cartRepository;

	@Override
	public void save(Cart cart) {
		cartRepository.save(cart);
	}

	@Override
	public Optional<Cart> findByProductAndUser(Product product, User user) {
		return cartRepository.findCartByProductAndUser(product, user);
	}

//	@Override
//	public List<Cart> findCartByUser(User user) {
//		PageRequest productPageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "title"));
//		Page<Product> productPage=null;
//		return cartRepository.findCartByUser(user);
//	}

	@Override
	public List<Cart> findCartByUser(User user) {
		//PageRequest productPageable = PageRequest.of(pageNumber, szie);
		//Sort.by(Direction.DESC, "createAt").and(Sort.by(Direction.DESC, "id"))
		List<Cart> cartList=cartRepository.findCartByUser(user);
		Collections.sort(cartList, new Cart.UpdateAtComparator());
		return cartList;
	}


	@Override
	public void updateQuantity(Cart cart) {
		 cartRepository.save(cart);
	}

	@Override
	public Optional<Cart> getCart(Integer cartId) {
		return cartRepository.findById(cartId);
		
	}

	@Override
	public void deleteCart(Integer Card) {
		 cartRepository.deleteById(Card);
	}
}
