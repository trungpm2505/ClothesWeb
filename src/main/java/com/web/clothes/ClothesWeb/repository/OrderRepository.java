package com.web.clothes.ClothesWeb.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.clothes.ClothesWeb.entity.Order;
import com.web.clothes.ClothesWeb.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>{
	Page<Order> findAll(Pageable pageable);
	Page<Order> findOrderByStatus(Pageable pageable,int status);
	public Optional<Order> getOrderById(Integer orderId);
	
	 
	 @Query("SELECT o FROM Order o WHERE (:createAt IS NULL OR o.createAt = :createAt) AND (:startDate IS NULL OR o.createAt >= :startDate) AND (:endDate IS NULL OR o.createAt <= :endDate) AND (:keyword IS NULL OR o.user.userName LIKE %:keyword% OR o.fullName LIKE %:keyword% OR o.phone LIKE %:keyword% OR o.adrress LIKE %:keyword% OR o.note LIKE %:keyword% OR o.id LIKE %:keyword% OR o.totalMoney LIKE %:keyword% OR o.note LIKE %:keyword%) AND (:status = 0 OR o.status = :status)")
	 Page<Order> findByDateRangeAndKeyword(Pageable pageable, LocalDate createAt, LocalDate startDate, LocalDate endDate, String keyword, int status);
	 
	 
	 //user
	Page<Order> findByUser(Pageable pageable,User user);
	Page<Order> findOrderByStatusAndUser(Pageable pageable,int status,User user);
	
	//report 
	 @Query("SELECT o FROM Order o WHERE (:completedAt IS NULL OR o.completedAt = :completedAt) AND (:startDate IS NULL OR o.completedAt >= :startDate) AND (:endDate IS NULL OR o.completedAt <= :endDate) AND (:keyword IS NULL OR o.user.userName LIKE %:keyword% OR o.fullName LIKE %:keyword% OR o.phone LIKE %:keyword% OR o.adrress LIKE %:keyword% OR o.note LIKE %:keyword% OR o.id LIKE %:keyword% OR o.totalMoney LIKE %:keyword% OR o.note LIKE %:keyword%) AND (o.status = 4)")
	 Page<Order> findOrderForReport(Pageable pageable, LocalDate completedAt, LocalDate startDate, LocalDate endDate, String keyword);
	
	 //user
//	@Query("SELECT o.* FROM orders o join order_details od on o.id = od.order_id join product p on od.product_id = p.id  where o.user_id = :user.id AND  ( o.id LIKE %:keyword% OR p.title LIKE %:keyword%)")
	 @Query("SELECT o FROM Order o WHERE (o.user = :user) AND (:keyword IS NULL OR o.user.userName LIKE %:keyword% OR o.id LIKE %:keyword% )")
	 Page<Order> findByKeywordForUser(Pageable pageable,User user, String keyword);
}
//	 
