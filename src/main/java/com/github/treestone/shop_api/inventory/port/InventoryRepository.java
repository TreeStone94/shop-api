package com.github.treestone.shop_api.inventory.port;

import com.github.treestone.shop_api.inventory.domain.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Optional<Inventory> findByProductId(Long productId);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("SELECT i FROM Inventory i WHERE i.product.id = :productId")
	Optional<Inventory> findByProductIdWithOptimisticLock(Long productId);
}
