package com.web.clothes.ClothesWeb.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class Rate {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	LocalDateTime createAt = LocalDateTime.now();
	
    
	@Column(nullable = false)
    private int rating;
	@Column(nullable = false)
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false, referencedColumnName = "id")
    private User user;
    
    @OneToMany(mappedBy = "rate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResponseRate> responses = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Image> images = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "id")
    private Order order;
    
    
}
