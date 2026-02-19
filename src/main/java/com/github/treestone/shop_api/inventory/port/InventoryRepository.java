package com.github.treestone.shop_api.inventory.port;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Inventory findByProductId(Long productId);
}
