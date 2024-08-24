package com.web.clothes.ClothesWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Rate;
import com.web.clothes.ClothesWeb.entity.ResponseRate;

@Repository
public interface ResponseRepository extends JpaRepository<ResponseRate, Integer> {
	public List<ResponseRate> getResponsesByRate(Rate rate);
}
