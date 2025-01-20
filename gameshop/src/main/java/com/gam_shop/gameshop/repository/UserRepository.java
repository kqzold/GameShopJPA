package com.gam_shop.gameshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gam_shop.gameshop.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
}
