package com.web.clothes.ClothesWeb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.clothes.ClothesWeb.entity.Category;

public interface CategoryService {
	public Optional<Category> getCategory(Integer categoryId);
	public void save(Category category);
	public void updateCategory(Category category);
	public void deleteCategory(Category category);
	public Optional<Category> findCategoryByName(String categoryName);
	public Page<Category> getAllCategory(int pageNumber, int szie);
	public Page<Category> getCategoryByKey(int pageNumber, int szie,String keyWord);
	public List<Category> getAll();
}
