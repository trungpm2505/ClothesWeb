package com.web.clothes.ClothesWeb.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.web.clothes.ClothesWeb.dto.responseDto.ImageResponseDto;
import com.web.clothes.ClothesWeb.entity.Image;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.Rate;

public interface ImageService {
	public void save(Image image);
	public void uploadFile(MultipartFile[] files,int defaultFileIndex,Product product);
	public void uploadFileForRate(MultipartFile[] files,Rate rate);
	public List<ImageResponseDto> getImageByProduct(Product product);
	public List<ImageResponseDto> getImageByRate(Rate rate);
	public ImageResponseDto getImageByProductAndDefault(Product product);
	public void deleteByProduct(Product product);
	public void deleteByRate(Rate rate);
}
