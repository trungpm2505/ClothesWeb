package com.web.clothes.ClothesWeb.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.ProductAttributeValue;
import com.web.clothes.ClothesWeb.repository.ProductAttributeValueRepository;
import com.web.clothes.ClothesWeb.dto.responseDto.ProductAttributeValueResponseDto;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ProductAttributeValueServiceImpl implements ProductAttributeValueService{
	private final ProductAttributeValueRepository productAttributeValueRepository;
	@Override
	public void save(ProductAttributeValue productAttributeValue) {
		productAttributeValueRepository.save(productAttributeValue);
	}
	@Override
	public ProductAttributeValueResponseDto findProductAttributeValueByProduct(Product product) {
		
		Optional<ProductAttributeValue> productAttributeValue= productAttributeValueRepository.findProductAttributeValueByProduct(product);
		ProductAttributeValueResponseDto  productAttributeValueResponseDto= new ProductAttributeValueResponseDto();
		if(productAttributeValue.isPresent()) {
			productAttributeValueResponseDto.setId(productAttributeValue.get().getId());
			if(productAttributeValue.get().getSize()!=null) {
				productAttributeValueResponseDto.setSizeId(productAttributeValue.get().getSize().getId());
				productAttributeValueResponseDto.setSize(productAttributeValue.get().getSize().getAttributeValueName());
			}
			if(productAttributeValue.get().getColor()!=null) {
				productAttributeValueResponseDto.setColorId(productAttributeValue.get().getColor().getId());
				productAttributeValueResponseDto.setColor(productAttributeValue.get().getColor().getAttributeValueName());
			}
			
		}
		
		return productAttributeValueResponseDto;
	}
	@Override
	public void deleteById(Integer id) {
		productAttributeValueRepository.deleteById(id);
		
	}
//	@Override
//	public void setDeleteAt(Product product) {
//		System.out.println("productAttr set delete");
//		Optional<ProductAttributeValue> productAttributeValue= productAttributeValueRepository.findProductAttributeValueByProduct(product);
//		if(productAttributeValue.isPresent()) {
//			productAttributeValue.get().setDeleteAt(LocalDate.now());
//			save(productAttributeValue.get());
//		}
//		
//	}

}
