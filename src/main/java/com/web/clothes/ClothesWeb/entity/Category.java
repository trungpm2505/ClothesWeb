package com.web.clothes.ClothesWeb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 20)
	private String categoryName;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate updateAt;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deleteAt;


	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();
}
