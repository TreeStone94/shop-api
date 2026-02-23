package com.github.treestone.shop_api.inventory.usecase;

import com.github.treestone.shop_api.inventory.processor.InventoryProcessor;
import com.github.treestone.shop_api.product.domain.Product;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecreaseStockWithOptimisticLockUseCase {
	private static final int MAX_RETRY_COUNT = 3;
	private final InventoryProcessor inventoryProcessor;

	public record Input(@NotNull Long productId) {}
	public record Output(Product product) {}

	public Output execute(Input input) {
		int retryCount = 0;

		while (retryCount < MAX_RETRY_COUNT) {
			try {
				return new Output(inventoryProcessor.decreaseStockWithOptimisticLock(input.productId()).getProduct());
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
}
