package com.web.clothes.ClothesWeb.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Attribute;
import com.web.clothes.ClothesWeb.entity.AttributeValue;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Integer> {
	@Query("SELECT a FROM AttributeValue a WHERE a.deleteAt is null AND a.id = :AttributeValueId")
	public Optional<AttributeValue> getAttributeValueById(Integer AttributeValueId);
	@Query("SELECT a FROM AttributeValue a WHERE a.deleteAt is null AND a.attributeValueName = :attibuteName")
	public Optional<AttributeValue> getAttributeValueByAttributeValueNameIgnoreCase(String attibuteName);

	//public void deleteAttributeValueById(Integer attributeValueId);

//public Page<AttributeValue> findAttributeValueByAttribute(Pageable Page,Attribute attribute);
	@Query("SELECT a FROM AttributeValue a WHERE a.deleteAt is null AND a.attribute = :attribute")
	Page<AttributeValue> findAttributeValuePage(Pageable pageable, @Param("attribute") Attribute attribute);
	
	@Query("SELECT a FROM AttributeValue a WHERE a.deleteAt is null AND a.attribute = :attribute")
	List<AttributeValue> getCategoryList(@Param("attribute") Attribute attribute);
	
	@Query("SELECT a FROM AttributeValue a WHERE a.deleteAt is null AND a.attribute = :attribute AND a.attributeValueName LIKE %:keyword%")
	 Page<AttributeValue> findByKeyword(Pageable pageable, @Param("attribute") Attribute attribute,String keyword);
}
