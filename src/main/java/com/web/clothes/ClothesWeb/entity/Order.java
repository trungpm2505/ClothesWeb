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
import javax.persistence.Table;

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
@Table(name="orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 60)
	private String fullName;
	
	@Column(nullable = false, length = 11)
	private String phone;
	
	@Column(nullable = false, length = 100)
	private String adrress;
	
	@Column(nullable = true, length = 50)
	private String note;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();
	
	@Column(nullable = true)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate completedAt;
	
	@Column(nullable = false)
	private int status=1;
	
	@Column(nullable = false)
	private float totalMoney;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true, referencedColumnName = "id")
	private User user;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderDetails> orderDetailsSet = new ArrayList<>();
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Rate> rates = new ArrayList<>();
}
