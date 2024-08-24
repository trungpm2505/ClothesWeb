package com.web.clothes.ClothesWeb.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.web.clothes.ClothesWeb.entity.Feedback;

public interface FeedbackService {
	
	public Optional<Feedback> getFeedback(Integer feedbackId);
	public void deleteFeedback(Feedback feedback);
	public void save(Feedback feedback);
	public Page<Feedback> getAllFeedback(int pageNumber, int szie);
	
	public Page<Feedback> getFeedbackByKey(int pageNumber, int size,String keyWord);
}
