package com.github.treestone.shop_api.order.usecase;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import com.github.treestone.shop_api.inventory.port.InventoryRepository;
import com.github.treestone.shop_api.order.domain.Order;
import com.github.treestone.shop_api.order.port.OrderRepository;
import com.github.treestone.shop_api.user.domain.User;
import com.github.treestone.shop_api.user.port.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOrderUseCase {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final InventoryRepository inventoryRepository;

	public record Input(@NotNull Long productId, @NotNull Long userId) {}

	public void execute(Input input) {
		Optional<Inventory> inventory = inventoryRepository.findByProductId(input.productId);

		if (inventory.isPresent()) {
			inventory.get().getStockQuantity().decreaseStockQuantity();

			User user = userRepository.findById(input.userId).orElseThrow(() ->  new IllegalArgumentException("User not found"));

			Order order = Order.builder()
					.product(inventory.get().getProduct())
					.user(user)
					.build();

			orderRepository.save(order);
		} else {
			throw new IllegalArgumentException("Product not found");
		}

	}
}
