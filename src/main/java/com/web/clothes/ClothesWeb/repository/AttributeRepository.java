package com.web.clothes.ClothesWeb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute,Integer>{
	
	public Optional<Attribute> getAttributeById(Integer attributeId);
	
	public Optional<Attribute> getAttributeByAttributeName(String attributeName);
}
