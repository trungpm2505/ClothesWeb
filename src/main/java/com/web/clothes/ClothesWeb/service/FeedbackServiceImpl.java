package com.web.clothes.ClothesWeb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Feedback;
import com.web.clothes.ClothesWeb.repository.FeedbackRepository;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	@Autowired
	private FeedbackRepository feedbackRepository;

	@Override
	public Page<Feedback> getAllFeedback(int pageNumber, int szie) {
		PageRequest feedbackPageable = PageRequest.of(pageNumber, szie, Sort.by(Sort.Direction.ASC, "fullName"));

		Page<Feedback> feedbackPage = feedbackRepository.findFeedbackPage(feedbackPageable);

		return feedbackPage;
	}

	@Override
	public Optional<Feedback> getFeedback(Integer feedbackId) {
		Optional<Feedback> feedback = feedbackRepository.getFeedbackById(feedbackId);
		return feedback;
	}

	@Override
	public void deleteFeedback(Feedback feedback) {
		feedbackRepository.delete(feedback);
	}

	@Override
	public void save(Feedback feedback) {
		feedbackRepository.save(feedback);
	}

	@Override
	public Page<Feedback> getFeedbackByKey(int pageNumber, int size, String keyWord) {
		PageRequest feedbackPageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, "fullName"));

		Page<Feedback> feedbackPage = feedbackRepository.findByKeyword(feedbackPageable, keyWord);
		return feedbackPage;
	}
}
