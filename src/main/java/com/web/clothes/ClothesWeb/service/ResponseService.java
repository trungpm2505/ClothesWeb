package com.web.clothes.ClothesWeb.service;

import java.util.List;

import com.web.clothes.ClothesWeb.dto.responseDto.ResponseResponseDto;
import com.web.clothes.ClothesWeb.entity.Rate;
import com.web.clothes.ClothesWeb.entity.ResponseRate;

public interface ResponseService {
	public List<ResponseResponseDto> getAll(Rate rate);
	public void save(ResponseRate response);
}
