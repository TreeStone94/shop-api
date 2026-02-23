package com.github.treestone.shop_api.inventory.processor;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import com.github.treestone.shop_api.inventory.port.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryProcessor {
	private final InventoryRepository inventoryRepository;

	@Transactional
	public Inventory decreaseStockWithOptimisticLock(Long productId) {
		Inventory inventory = inventoryRepository
				.findByProductIdWithOptimisticLock(productId)
				.orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다"));

		inventory.getStockQuantity().decreaseStockQuantity();

		return inventory;
	}
}
