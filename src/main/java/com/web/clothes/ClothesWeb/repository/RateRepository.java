package com.web.clothes.ClothesWeb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.Product;
import com.web.clothes.ClothesWeb.entity.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Integer> {
	Page<Rate> findRateByProduct(Pageable pageable,Product product);
	Page<Rate> findRateByProductAndRating(Pageable pageable,Product product,int rating);
	Rate findRateById(Integer id);
	public Rate findRateByOrderAndProduct(Order order,Product ptoduct);
	public List<Rate> findRateByOrder(Order order);
}
