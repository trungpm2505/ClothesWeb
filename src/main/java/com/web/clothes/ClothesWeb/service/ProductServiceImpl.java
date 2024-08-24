package com.web.clothes.ClothesWeb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.repository.ProductRepository;

@Service

public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductRepository productRepository;


	@Override
	public Optional<Product> getProduct(Integer productId) {
		Optional<Product> product = productRepository.getProductById(productId);
		return product;
	}

	@Override
	public void save(Product product) {
		productRepository.save(product);
		 
	}


	@Override
	public Optional<Product> getProductByTitle(String title) {
		Optional<Product> product = productRepository.getProductByTitle(title);
		return product;
	}

	@Override
	public Page<Product> getProductPage(int pageNumber, int szie,int type) {
		PageRequest productPageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "title"));
		Page<Product> productPage=null;
		if(type==1) {
			productPage = productRepository.findProductPage(productPageable);
		}else if(type==2) {
			productPage = productRepository.findMenProductPage(productPageable);
		}else if(type==3) {
			productPage = productRepository.findWomenProductPage(productPageable);
		}else if(type==4) {
			productPage = productRepository.findProductPage(productPageable);
		}
		
		 
		return productPage;
	}

	@Override
	public void delete(Product product) {
		product.setDeleteAt(LocalDate.now());
		productRepository.save(product);
		
	}

	@Override
	public List<Product> getAllProduct() {
		return productRepository.findAllProduct();
	}

	@Override
	public Page<Product> searchProduct(int pageNumber, int szie, int type, Category category, String keyword) {
		PageRequest productPageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "title"));
		Page<Product> productPage=null;
		
			productPage = productRepository.searchProduct(productPageable,type,category,keyword);
		
		 
		return productPage;
	}
	
	
}
