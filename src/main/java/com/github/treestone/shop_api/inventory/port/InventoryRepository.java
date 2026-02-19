package com.github.treestone.shop_api.inventory.port;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
