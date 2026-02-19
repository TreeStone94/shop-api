package com.github.treestone.shop_api.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockQuantity {
	@Column(nullable = false)
	private Long quantity;
}
