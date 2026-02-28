package com.github.treestone.shop_api.order.usecase;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import com.github.treestone.shop_api.inventory.port.InventoryRepository;
import com.github.treestone.shop_api.order.domain.Order;
import com.github.treestone.shop_api.order.port.OrderRepository;
import com.github.treestone.shop_api.user.domain.User;
import com.github.treestone.shop_api.user.port.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateOrderWithOptimisticLockUseCase {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final InventoryRepository inventoryRepository;

	public record Input(@NotNull Long userId, @NotNull Long productId) { }

	@Retryable(
			retryFor = ObjectOptimisticLockingFailureException.class,
			maxAttempts = 3,
			backoff = @Backoff(delay = 100)
	)
	public void execute(Input input) {
		User user = userRepository.findById(input.userId())
				.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		Inventory inventory = inventoryRepository.findByProductIdWithOptimisticLock(input.productId())
				.orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다"));

		inventory.getStockQuantity().decreaseStockQuantity();

		Order order = Order.builder()
				.product(inventory.getProduct())
				.user(user)
				.build();

		orderRepository.save(order);

	}

	@Recover
	public void recover(ObjectOptimisticLockingFailureException e, Input input) {
		log.warn("낙관적 락 재시도 모두 실패: productId={}",input.productId());
		throw new RuntimeException("잠시 후 다시 시도해주세요.");
	}
}
