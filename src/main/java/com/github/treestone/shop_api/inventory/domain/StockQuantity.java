package com.github.treestone.shop_api.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantity {
	@Column(nullable = false)
	private Long quantity;

	public void decreaseStockQuantity() {
		if (quantity <= 0) {
			throw new IllegalStateException("Stock quantity cannot be less than zero");
		}
		this.quantity = this.quantity - 1;
	}
}
