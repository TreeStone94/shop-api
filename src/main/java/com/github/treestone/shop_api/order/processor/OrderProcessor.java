package com.github.treestone.shop_api.order.processor;

import com.github.treestone.shop_api.inventory.usecase.DecreaseStockWithOptimisticLockUseCase;
import com.github.treestone.shop_api.order.usecase.CreateOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderProcessor {

	private final DecreaseStockWithOptimisticLockUseCase decreaseStockWithOptimisticLockUseCase;
	private final CreateOrderUseCase createOrderUseCase;

	@Transactional
	public void createOrderWithStockDecrease(Long productId, Long userId) {
		// 1. 재고 감소 (낙관적 락 + 재시도 로직 포함)
		DecreaseStockWithOptimisticLockUseCase.Output stockOutput =
			decreaseStockWithOptimisticLockUseCase.execute(
				new DecreaseStockWithOptimisticLockUseCase.Input(productId)
			);

		// 2. 주문 생성 (실패 시 재고 감소도 함께 롤백됨)
		createOrderUseCase.execute(
			new CreateOrderUseCase.Input(userId, stockOutput.product())
		);
	}
}
