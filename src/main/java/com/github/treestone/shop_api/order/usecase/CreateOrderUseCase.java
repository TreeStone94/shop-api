package com.github.treestone.shop_api.order.usecase;

import com.github.treestone.shop_api.order.domain.Order;
import com.github.treestone.shop_api.order.port.OrderRepository;
import com.github.treestone.shop_api.product.domain.Product;
import com.github.treestone.shop_api.user.domain.User;
import com.github.treestone.shop_api.user.port.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOrderUseCase {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;

	public record Input(@NotNull Long userId, @NotNull Product product) {}

	public void execute(Input input) {
		User user = userRepository.findById(input.userId())
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		Order order = Order.builder()
				.product(input.product())
				.user(user)
				.build();

		orderRepository.save(order);

	}
}
