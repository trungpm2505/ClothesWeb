package com.web.clothes.ClothesWeb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Category;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{
	@Query("SELECT c FROM Category c WHERE c.deleteAt is null AND c.id = :categoryId")
	public Optional<Category> getCategoryById(Integer categoryId);
	
	@Query("SELECT c FROM Category c WHERE c.deleteAt is null AND c.categoryName = :categoryName")
	public Optional<Category> getCategoryByCategoryNameIgnoreCase(String categoryName);
	
	
	@Query("SELECT c FROM Category c WHERE c.deleteAt is null")
    List<Category> findAll();
	
	@Query("SELECT c FROM Category c WHERE c.deleteAt is null")
    Page<Category> findCategoryPage(Pageable pageable);
	
	@Query("SELECT c FROM Category c WHERE c.deleteAt is null AND c.categoryName LIKE %:keyword%")
	 Page<Category> findByKeyword(Pageable pageable,String keyword);

}
