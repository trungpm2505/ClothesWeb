package com.web.clothes.ClothesWeb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Product;

public interface ProductService {
	public Optional<Product> getProduct(Integer prodcutId);
	public Optional<Product> getProductByTitle(String title);
	public void save(Product product);
	public void delete(Product product);
	public Page<Product> getProductPage(int pageNumber, int szie,int type);
	public List<Product> getAllProduct();
	public Page<Product> searchProduct(int pageNumber, int szie,int type,Category category,String keyword);
	//public List<ProductResponseDto> getBestSeller();
}
