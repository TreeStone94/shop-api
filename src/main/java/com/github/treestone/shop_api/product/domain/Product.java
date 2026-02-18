package com.github.treestone.shop_api.product.domain;

import jakarta.persistence.*;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(nullable = false)
	String name;
	String description;
	String image;
}
