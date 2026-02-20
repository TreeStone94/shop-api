package com.github.treestone.shop_api.order.port;

import com.github.treestone.shop_api.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
