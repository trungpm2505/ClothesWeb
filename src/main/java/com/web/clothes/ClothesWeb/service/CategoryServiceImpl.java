package com.web.clothes.ClothesWeb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public Optional<Category> getCategory(Integer categoryId) {
		Optional<Category> category = categoryRepository.getCategoryById(categoryId);
		return category;
	}

	@Override
	public void save(Category category) {
		categoryRepository.save(category);

	}

	@Override
	public void updateCategory(Category category) {
		category.setUpdateAt(LocalDate.now());
		categoryRepository.save(category);
	}

	@Override
	public void deleteCategory(Category category) {

		// set date delete
		category.setDeleteAt(LocalDate.now());
		categoryRepository.save(category);

	}

	@Override
	public Optional<Category> findCategoryByName(String categoryName) {
		Optional<Category> category = categoryRepository.getCategoryByCategoryNameIgnoreCase(categoryName);
		return category;
	}

	@Override
	public Page<Category> getAllCategory(int pageNumber, int szie) {

		PageRequest categoryPageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "categoryName"));

		Page<Category> categoryPage = categoryRepository.findCategoryPage(categoryPageable);

		return categoryPage;
	}

	@Override
	public List<Category> getAll() {
		List<Category> categories = categoryRepository.findAll();
		return categories;
	}

	@Override
	public Page<Category> getCategoryByKey(int pageNumber, int szie, String keyWord) {
		PageRequest categoryPageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "categoryName"));

		return categoryRepository.findByKeyword(categoryPageable, keyWord);
	}
}
