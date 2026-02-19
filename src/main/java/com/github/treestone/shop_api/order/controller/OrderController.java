package com.github.treestone.shop_api.order.controller;

import com.github.treestone.shop_api.order.usecase.CreateOrderUseCase;
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

	@PostMapping
	public ResponseEntity createOrder(@RequestBody CreateOrderUseCase.Input input) {
		createOrderUseCase.execute(input);
		return ResponseEntity.ok().build();
	}
}
