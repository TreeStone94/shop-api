package com.github.treestone.shop_api.product.usecase;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import com.github.treestone.shop_api.inventory.domain.StockQuantity;
import com.github.treestone.shop_api.inventory.port.InventoryRepository;
import com.github.treestone.shop_api.product.domain.Product;
import com.github.treestone.shop_api.product.port.ProductRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterProductUseCase {
	private final ProductRepository productRepository;
	private final InventoryRepository inventoryRepository;

	public record Input(@NotBlank String name, @NotBlank String description, String image, @NotNull Long quantity) {}

	public void execute(Input input) {
		Product product = Product.builder()
				.name(input.name)
				.description(input.description)
				.image(input.image)
				.build();

		productRepository.save(product);

		StockQuantity stockQuantity = StockQuantity.builder()
				.quantity(input.quantity)
				.build();

		Inventory inventory = Inventory.builder()
				.product(product)
				.stockQuantity(stockQuantity)
				.build();

		inventoryRepository.save(inventory);
	}
}
