package com.github.treestone.shop_api.order.controller;

import com.github.treestone.shop_api.inventory.usecase.DecreaseStockUseCase;
import com.github.treestone.shop_api.inventory.usecase.DecreaseStockWithOptimisticLockUseCase;
import com.github.treestone.shop_api.inventory.usecase.DecreaseStockWithPessimisticLockUseCase;
import com.github.treestone.shop_api.order.controller.request.CreateOrderBody;
import com.github.treestone.shop_api.order.usecase.CreateOrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	private final CreateOrderUseCase createOrderUseCase;
	private final DecreaseStockUseCase decreaseStockUseCase;
	private final DecreaseStockWithOptimisticLockUseCase decreaseStockWithOptimisticLockUseCase;
	private final DecreaseStockWithPessimisticLockUseCase decreaseStockWithPessimisticLockUseCase;

	@PostMapping
	public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderBody body) {
//		DecreaseStockUseCase.Output output = decreaseStockUseCase.execute(
//				new DecreaseStockUseCase.Input(body.productId())
//		);

		DecreaseStockWithOptimisticLockUseCase.Output output = decreaseStockWithOptimisticLockUseCase.execute(
				new DecreaseStockWithOptimisticLockUseCase.Input(body.productId())
		);

//		DecreaseStockWithPessimisticLockUseCase.Output output = decreaseStockWithPessimisticLockUseCase.execute(
//				new DecreaseStockWithPessimisticLockUseCase.Input(body.productId())
//		);

		CreateOrderUseCase.Input createOrderUseCaseInput = new CreateOrderUseCase.Input(
				body.userId(),
				output.product()
		);
		createOrderUseCase.execute(createOrderUseCaseInput);
		return ResponseEntity.ok().build();
	}

}
