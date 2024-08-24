package com.web.clothes.ClothesWeb.service;

import java.util.List;
import java.util.Optional;


import com.web.clothes.ClothesWeb.dto.responseDto.AttributeValuePageResponseDto;
import com.web.clothes.ClothesWeb.entity.AttributeValue;

public interface AttributeValueService {
	public Optional<AttributeValue> getAttributeValue(Integer attributeValueId);
	public void save(AttributeValue attributeValue);
	public void updateAttributeValue(AttributeValue attributeValue);
	public void deleteAttributeValue(AttributeValue attributeValue);
	public Optional<AttributeValue> findAttributeValueByName(String attributeValueName);
	public AttributeValuePageResponseDto getAttributeValueByAttribute(int pageNumber, int szie,String attributeName,String key);
	//public AttributeValuePageResponseDto getAttributeValueByAttributeAndKey(int pageNumber, int szie,String attributeName,String key);
	public List<AttributeValue> getList(String attributeName);
//	public Optional<AttributeValue> findAttributeValueByKey(String name );
}
