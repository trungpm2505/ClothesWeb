package com.web.clothes.ClothesWeb.service;

import java.util.Optional;

import com.web.clothes.ClothesWeb.entity.Attribute;


public interface AttributeService {
	public Optional<Attribute> getAttribute(String attributeName);
}
