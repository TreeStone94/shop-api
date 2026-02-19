package com.github.treestone.shop_api.inventory.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantity {
	@Column(nullable = false)
	private Long quantity;
}
