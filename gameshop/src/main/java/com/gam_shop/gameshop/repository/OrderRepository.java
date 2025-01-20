package com.gam_shop.gameshop.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gam_shop.gameshop.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findAllByOrderDateBetween(LocalDateTime start, LocalDateTime end);

}
