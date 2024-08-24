package com.web.clothes.ClothesWeb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class ProductAttributeValue {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne
    @JoinColumn(name = "size_id")
    private AttributeValue size;
    
    @ManyToOne
    @JoinColumn(name = "color_id")
    private AttributeValue color;
    

	
	public ProductAttributeValue(Product product, AttributeValue size, AttributeValue color) {
		super();
		this.product = product;
		this.size = size;
		this.color = color;
	}

	
	
	
    
    
}
