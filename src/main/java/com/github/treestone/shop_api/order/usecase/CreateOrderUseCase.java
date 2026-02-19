package com.github.treestone.shop_api.order.usecase;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import com.github.treestone.shop_api.inventory.domain.StockQuantity;
import com.github.treestone.shop_api.inventory.port.InventoryRepository;
import com.github.treestone.shop_api.order.domain.Order;
import com.github.treestone.shop_api.order.port.OrderRepository;
import com.github.treestone.shop_api.user.domain.User;
import com.github.treestone.shop_api.user.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final InventoryRepository inventoryRepository;

	public record Input(Long productId, Long userId) {}

	public void execute(Input input) {
		Inventory inventory = inventoryRepository.findByProductId(input.productId);

		if (inventory != null) {
			StockQuantity stockQuantity = inventory.getStockQuantity();
			Long quantity = stockQuantity.getQuantity();
			quantity -= 1;
			stockQuantity.setQuantity(quantity);
			inventory.setStockQuantity(stockQuantity);

			User user = userRepository.findById(input.userId).orElse(null);

			Order order = Order.builder()
					.product(inventory.getProduct())
					.user(user)
					.build();

			orderRepository.save(order);
		}

	}
}
