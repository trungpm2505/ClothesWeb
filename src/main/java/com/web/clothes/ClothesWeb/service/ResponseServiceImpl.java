package com.web.clothes.ClothesWeb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.dto.responseDto.ResponseResponseDto;
import com.web.clothes.ClothesWeb.dto.responseDto.UserResponseDto;
import com.web.clothes.ClothesWeb.entity.Rate;
import com.web.clothes.ClothesWeb.entity.ResponseRate;
import com.web.clothes.ClothesWeb.repository.ResponseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService{
	
	private final ResponseRepository responseRepository;

	@Override
	public List<ResponseResponseDto> getAll(Rate rate) {
		
		 List<ResponseRate> responses = responseRepository.getResponsesByRate(rate);
		 List<ResponseResponseDto> responseResponseDtos = responses.stream()
					.map(res -> new ResponseResponseDto(res.getId(), new UserResponseDto(res.getUser().getUserName(),res.getUser().getRole().getRoleName()),
							res.getContent()))
					.collect(Collectors.toList());
		 for (ResponseResponseDto responseResponseDto : responseResponseDtos) {
			System.out.println("getAll response"+responseResponseDto.getContent());
		}
		 return responseResponseDtos;
	}

	@Override
	public void save(ResponseRate response) {
		responseRepository.save(response);		
	}
}
