package com.github.treestone.shop_api.order.controller;

import com.github.treestone.shop_api.order.usecase.CreateOrderWithOptimisticLockUseCase;
import com.github.treestone.shop_api.order.usecase.CreateOrderWithPessimisticLockUseCase;
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
	private final CreateOrderWithOptimisticLockUseCase createOrderWithOptimisticLockUseCase;
	private final CreateOrderWithPessimisticLockUseCase createOrderWithPessimisticLockUseCase;

	@PostMapping("/with-optimistic-lock")
	public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderWithOptimisticLockUseCase.Input input) {
		createOrderWithOptimisticLockUseCase.execute(input);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/with-pessimistic-lock")
	public ResponseEntity<Void> createOrderWithPessimisticLock(@Valid @RequestBody CreateOrderWithPessimisticLockUseCase.Input input) {
		createOrderWithPessimisticLockUseCase.execute(input);
		return ResponseEntity.ok().build();
	}
}
