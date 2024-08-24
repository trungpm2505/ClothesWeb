package com.web.clothes.ClothesWeb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.Rate;
import com.web.clothes.ClothesWeb.repository.RateRepository;
@Service
public class RateServiceImpl implements RateService{
	@Autowired
	private RateRepository rateRepository;
	@Override
	public Page<Rate> finPage(int pageNumber, int szie,Product product) {
		PageRequest ratePageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.DESC, "createAt"));
		return rateRepository.findRateByProduct(ratePageable,product);
		
	}

	@Override
	public void save(Rate rate) {
		rateRepository.save(rate);
		
	}

	@Override
	public Rate getRateById(Integer rateId) {
		return rateRepository.findRateById(rateId);
	}

	@Override
	public Page<Rate> finPageByRateScore(int pageNumber, int szie, Product product, int rateScore) {
		PageRequest ratePageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.DESC, "createAt"));
		return rateRepository.findRateByProductAndRating(ratePageable,product,rateScore);
		
	}

	@Override
	public Rate finPageByOrderAndProduct(Order order,Product ptoduct) {
		return rateRepository.findRateByOrderAndProduct(order, ptoduct);
	}

	@Override
	public List<Rate> getRateByOrder(Order order) {
		return rateRepository.findRateByOrder(order);
	}

}
