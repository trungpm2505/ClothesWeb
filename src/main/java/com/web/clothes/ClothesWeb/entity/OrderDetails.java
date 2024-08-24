package com.web.clothes.ClothesWeb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private float price;
	
	@Column(nullable = true)
	private float curentPrice;
	
	@Column(nullable = false)
	private int number;
	
	@Column(nullable = false)
	private float totalMoney;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,referencedColumnName = "id")
    private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
    private Product product;
}
