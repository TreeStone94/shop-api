package com.github.treestone.shop_api.order.controller.request;

import jakarta.validation.constraints.NotNull;

public record CreateOrderBody(@NotNull Long productId, @NotNull Long userId) {
}
