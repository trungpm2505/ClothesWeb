package com.web.clothes.ClothesWeb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.dto.responseDto.AttributeValuePageResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.AttributeValueResponseDto;
import com.web.clothes.ClothesWeb.entity.Attribute;
import com.web.clothes.ClothesWeb.entity.AttributeValue;
import com.web.clothes.ClothesWeb.repository.AttributeValueRepository;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AttributeValueServiceImpl implements AttributeValueService{
	
	private final AttributeValueRepository attributeValueRepository;
	
	private final AttributeService attributeService;

	@Override
	public Optional<AttributeValue> getAttributeValue(Integer attributeValueId) {
		Optional<AttributeValue> attributeValue = attributeValueRepository.getAttributeValueById(attributeValueId);
		return attributeValue;
	}

	@Override
	public void save(AttributeValue attributeValue) {
		attributeValueRepository.save(attributeValue);
	}

	@Override
	public void updateAttributeValue(AttributeValue attributeValue) {
		attributeValue.setUpdateAt(LocalDate.now());
		attributeValueRepository.save(attributeValue);
	}

	@Override
	public void deleteAttributeValue(AttributeValue attributeValue) {
		attributeValue.setDeleteAt(LocalDate.now());
		attributeValueRepository.save(attributeValue);
	}

	@Override
	public Optional<AttributeValue> findAttributeValueByName(String attributeValueName) {
		Optional<AttributeValue> attributeValue = attributeValueRepository.getAttributeValueByAttributeValueNameIgnoreCase(attributeValueName);
		return attributeValue;
	}

	@Override
	public AttributeValuePageResponseDto getAttributeValueByAttribute(int pageNumber, int szie,String attributeName,String key) {
		Optional<Attribute> attribute =attributeService.getAttribute(attributeName);
		PageRequest attributePageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "attributeValueName"));
		Page<AttributeValue> attributeValuePage = null;
		List<AttributeValueResponseDto> attributeValueResponseDto = new ArrayList<>();
		AttributeValuePageResponseDto attributeValuePageResponseDto=null;
		if(attribute.isPresent()) {
			if(key!=null && key.equals("")) {
				attributeValuePage = attributeValueRepository.findAttributeValuePage(attributePageable,attribute.get());
			}else {
				attributeValuePage = attributeValueRepository.findByKeyword(attributePageable,attribute.get(),key);
			}
			 
			 
				if(attributeValuePage!=null) {
					 attributeValueResponseDto = attributeValuePage.stream()
							.map(attributeValue -> new AttributeValueResponseDto(attributeValue.getId(),
									attributeValue.getAttributeValueName()))
							.collect(Collectors.toList());
					  attributeValuePageResponseDto = new AttributeValuePageResponseDto(
								attributeValuePage.getTotalPages(), attributeValuePage.getNumber(), attributeValuePage.getSize(),
								attributeValueResponseDto);
				}
				
		}
		
		return attributeValuePageResponseDto;
	}

	@Override
	public List<AttributeValue> getList(String attributeName) {
		Optional<Attribute> attribute =attributeService.getAttribute(attributeName);
		List<AttributeValue> AttributeValues =new ArrayList<>();
		if(attribute.isPresent()) {
			AttributeValues = attributeValueRepository.getCategoryList(attribute.get());
		}
		return AttributeValues;
	}

	


}
