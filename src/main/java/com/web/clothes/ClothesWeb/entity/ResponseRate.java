package com.web.clothes.ClothesWeb.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseRate {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDateTime createAt = LocalDateTime.now();
	
	 @Column(nullable = false)
	 private String content;
	
	@ManyToOne
    @JoinColumn(name = "rate_id",nullable = false, referencedColumnName = "id")
    private Rate rate;
    
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false, referencedColumnName = "id")
    private User user;
    
    
   
}
