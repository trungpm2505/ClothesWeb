package com.web.clothes.ClothesWeb.service;


import com.web.clothes.ClothesWeb.dto.responseDto.ProductAttributeValueResponseDto;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.ProductAttributeValue;

public interface ProductAttributeValueService {
	public void save(ProductAttributeValue productAttributeValue);
	
	public ProductAttributeValueResponseDto findProductAttributeValueByProduct(Product product);
	
	public void deleteById(Integer id);
//	public void setDeleteAt(Product product);
}
