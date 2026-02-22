package com.github.treestone.shop_api.order.usecase;


import com.github.treestone.shop_api.inventory.usecase.DecreaseStockUseCase;
import com.github.treestone.shop_api.inventory.usecase.DecreaseStockWithOptimisticLockUseCase;
import com.github.treestone.shop_api.inventory.usecase.DecreaseStockWithPessimisticLockUseCase;
import com.github.treestone.shop_api.order.domain.Order;
import com.github.treestone.shop_api.order.port.OrderRepository;
import com.github.treestone.shop_api.user.domain.User;
import com.github.treestone.shop_api.user.port.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOrderUseCase {
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
//	private final DecreaseStockUseCase decreaseStockUseCase;
//	private final DecreaseStockWithOptimisticLockUseCase decreaseStockWithOptimisticLockUseCase;
	private final DecreaseStockWithPessimisticLockUseCase decreaseStockWithPessimisticLockUseCase;

	public record Input(@NotNull Long productId, @NotNull Long userId) {
	}

	public void execute(Input input) {
//		DecreaseStockUseCase.Output output = decreaseStockUseCase.execute(
//				new DecreaseStockUseCase.Input(input.productId())
//		);

//		DecreaseStockWithOptimisticLockUseCase.Output output = decreaseStockWithOptimisticLockUseCase.execute(
//				new DecreaseStockWithOptimisticLockUseCase.Input(input.productId())
//		);

		DecreaseStockWithPessimisticLockUseCase.Output output = decreaseStockWithPessimisticLockUseCase.execute(
				new DecreaseStockWithPessimisticLockUseCase.Input(input.productId())
		);

		if (output != null) {
			User user = userRepository.findById(input.userId)
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			Order order = Order.builder()
					.product(output.product())
					.user(user)
					.build();

			orderRepository.save(order);
		} else {
			throw new IllegalArgumentException("Product not found");
		}

	}
}
