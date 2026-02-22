package com.github.treestone.shop_api.inventory.usecase;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import com.github.treestone.shop_api.inventory.port.InventoryRepository;
import com.github.treestone.shop_api.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DecreaseStockWithOptimisticLockUseCase {
	private static final int MAX_RETRY_COUNT = 3;
	private final InventoryRepository inventoryRepository;

	public record Input(@NotNull Long productId) {}
	public record Output(Product product) {}

	public Output execute(Input input) {
		int retryCount = 0;

		while (retryCount < MAX_RETRY_COUNT) {
			try {
				return decreaseStock(input);
			} catch (ObjectOptimisticLockingFailureException e) {
				retryCount++;

				if (retryCount >= MAX_RETRY_COUNT) {
					throw new RuntimeException("재고 감소 실패: 최대 재시도 횟수 초과", e);
				}

				// 백오프 전략: 재시도 간격을 점진적으로 늘림
				try {
					Thread.sleep(50L * retryCount); // 50ms, 100ms, 150ms
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("재시도 중 인터럽트 발생", ie);
				}
			}
		}

		throw new IllegalStateException("도달할 수 없는 코드");
	}

	@Transactional
	protected Output decreaseStock(Input input) {
		Inventory inventory = inventoryRepository
			.findByProductIdWithOptimisticLock(input.productId())
			.orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다"));

		inventory.getStockQuantity().decreaseStockQuantity();

		return new Output(inventory.getProduct());
	}
}
