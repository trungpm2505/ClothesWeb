package com.web.clothes.ClothesWeb.entity;

import java.time.LocalDateTime;
import java.util.Comparator;

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
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private int number;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime updatedAt = LocalDateTime.now();
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	public static class UpdateAtComparator implements Comparator<Cart> {
        public int compare(Cart cart1, Cart cart2) {
            LocalDateTime updateAt1 = cart1.getUpdatedAt();
            LocalDateTime updateAt2 = cart2.getUpdatedAt();
            
            // Implement your comparison logic here
            return updateAt2.compareTo(updateAt1); // Sort in descending order
        }
    }
}
