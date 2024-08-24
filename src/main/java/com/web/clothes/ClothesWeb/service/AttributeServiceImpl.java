package com.web.clothes.ClothesWeb.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Attribute;
import com.web.clothes.ClothesWeb.repository.AttributeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService{

	private final AttributeRepository attributeRepository;

	@Override
	public Optional<Attribute> getAttribute(String attributeName) {
		Optional<Attribute> attribute = attributeRepository.getAttributeByAttributeName(attributeName);
		return attribute;
	}
}
