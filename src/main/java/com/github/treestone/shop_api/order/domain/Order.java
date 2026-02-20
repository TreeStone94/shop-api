package com.github.treestone.shop_api.order.domain;

import com.github.treestone.shop_api.product.domain.Product;
import com.github.treestone.shop_api.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
