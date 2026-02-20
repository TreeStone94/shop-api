package com.github.treestone.shop_api.product.controller;

import com.github.treestone.shop_api.product.usecase.RegisterProductUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	private final RegisterProductUseCase registerProductUseCase;

	@PostMapping
	public ResponseEntity<Void> registerProduct(
			@Valid @RequestBody RegisterProductUseCase.Input input
	) {
		registerProductUseCase.execute(input);
		return ResponseEntity.ok().build();
	}
}
