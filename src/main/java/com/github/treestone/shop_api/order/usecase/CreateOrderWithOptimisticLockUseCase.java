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

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOrderWithOptimisticLockUseCase {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final InventoryRepository inventoryRepository;

	public record Input(@NotNull Long userId, @NotNull Long productId) {
	}

	public void execute(Input input) {
		Inventory inventory = inventoryRepository.findByProductIdWithPessimisticLock(input.productId())
				.orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다"));

		inventory.getStockQuantity().decreaseStockQuantity();

		User user = userRepository.findById(input.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Order order = Order.builder()
				.product(inventory.getProduct())
				.user(user)
				.build();

		orderRepository.save(order);

	}
}
