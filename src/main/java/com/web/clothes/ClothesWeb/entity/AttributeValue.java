package com.web.clothes.ClothesWeb.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class AttributeValue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 20)
	private String attributeValueName;

	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate createAt = LocalDate.now();

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate updateAt;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate deleteAt;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", nullable = false, referencedColumnName = "id")
	private Attribute attribute;
	
	
	

}
