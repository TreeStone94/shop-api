package com.github.treestone.shop_api.product.port;

import com.github.treestone.shop_api.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
