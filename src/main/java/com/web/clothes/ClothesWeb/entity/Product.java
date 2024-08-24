package com.web.clothes.ClothesWeb.entity;

import java.time.LocalDate;

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
import java.util.ArrayList;
import java.util.List;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 300)
	private String title;
	
	@Column(nullable = false)
	private Float price;
	
	@Column(nullable = true)
	private Float currentPrice;
	
	@Column(nullable = true)
	private int quantity;
	
	@Column(nullable = false)
	private boolean gender;
	
	@Column(nullable = false, length = 1500)
	private String discription;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate updateAt;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deleteAt;
	


	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")
	private Category category;
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> carts = new ArrayList<>();
	
	@OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetailsSet = new ArrayList<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Image> images = new ArrayList<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rate> rates = new ArrayList<>();
}
