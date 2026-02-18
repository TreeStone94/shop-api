package com.github.treestone.shop_api.user.port;


import com.github.treestone.shop_api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
