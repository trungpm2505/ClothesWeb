package com.web.clothes.ClothesWeb.service;



import java.util.List;

import org.springframework.data.domain.Page;

import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.Rate;

public interface RateService {
	
	public Page<Rate> finPage(int pageNumber, int szie,Product product);
	public Page<Rate> finPageByRateScore(int pageNumber, int szie,Product product,int rateScore);
	public Rate finPageByOrderAndProduct(Order order,Product ptoduct);

	public void save(Rate rate);
	public Rate getRateById(Integer rateId);
	public List<Rate> getRateByOrder(Order order);
	
}
