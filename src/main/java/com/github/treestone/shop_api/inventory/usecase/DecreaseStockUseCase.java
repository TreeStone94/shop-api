package com.github.treestone.shop_api.inventory.usecase;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import com.github.treestone.shop_api.inventory.port.InventoryRepository;
import com.github.treestone.shop_api.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DecreaseStockUseCase {
	private final InventoryRepository inventoryRepository;

	public record Input(@NotNull Long productId) {}
	public record Output(Product product) {}

	public Output execute(Input input) {
		Inventory inventory = inventoryRepository.findByProductId(input.productId)
				.orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다"));

		inventory.getStockQuantity().decreaseStockQuantity();

		return new Output(inventory.getProduct());
	}
}
