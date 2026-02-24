package com.github.treestone.shop_api.order.controller;

import com.github.treestone.shop_api.order.controller.request.CreateOrderBody;
import com.github.treestone.shop_api.order.processor.OrderProcessor;
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
	private final OrderProcessor orderProcessor;

	@PostMapping
	public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderBody body) {
		orderProcessor.createOrderWithStockDecrease(body.productId(), body.userId());
		return ResponseEntity.ok().build();
	}

}
