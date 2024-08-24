package com.web.clothes.ClothesWeb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.ProductAttributeValue;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue,Integer>{
	@Query("SELECT c FROM ProductAttributeValue c WHERE c.product = :product")
	public Optional<ProductAttributeValue> findProductAttributeValueByProduct(Product product);
}
