package com.web.clothes.ClothesWeb.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

	public Optional<Feedback> getFeedbackById(Integer feedbackId);

	@Query("SELECT c FROM Feedback c")
	Page<Feedback> findFeedbackPage(Pageable pageable);

	@Query("SELECT f FROM Feedback f WHERE f.fullName LIKE %:keyword%")
	Page<Feedback> findByKeyword(Pageable pageable, String keyword);
}
