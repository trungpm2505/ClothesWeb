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
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 255)
	private String inmageForShow;
	
	@Column(nullable = false, length = 255)
	private String inmageForSave;
	
	@Column(nullable = true)
	private Boolean isDefault = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true, referencedColumnName = "id")
    private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_id", nullable = true, referencedColumnName = "id")
    private Rate rate;
}

