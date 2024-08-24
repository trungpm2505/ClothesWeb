package com.web.clothes.ClothesWeb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Category;
import com.web.clothes.ClothesWeb.entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
	@Query("SELECT p FROM Product p WHERE p.deleteAt is null AND p.id = :productId")
	public Optional<Product> getProductById(Integer productId);
	
	@Query("SELECT p FROM Product p WHERE p.deleteAt is null AND p.title = :title")
	public Optional<Product> getProductByTitle(String title);
	@Query("SELECT p FROM Product p WHERE p.deleteAt is null")
    Page<Product> findProductPage(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.deleteAt is null and p.gender = false")
    Page<Product> findWomenProductPage(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.deleteAt is null and p.gender = true")
    Page<Product> findMenProductPage(Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE p.deleteAt is null")
    List<Product> findAllProduct();
	
	@Query("SELECT o FROM Product o WHERE o.deleteAt is null AND ((:type = 1 AND o.gender = true) OR (:type = 0 AND o.gender = false) OR (:type NOT IN (0, 1))) AND (:category IS NULL OR o.category = :category) AND (:keyword IS NULL OR o.title LIKE %:keyword% OR o.price LIKE %:keyword% OR o.currentPrice LIKE %:keyword% OR o.quantity LIKE %:keyword% OR o.discription LIKE %:keyword% )")
	Page<Product> searchProduct(Pageable pageable,int type,Category category,String keyword);
	
	
}
