package com.gam_shop.gameshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gam_shop.gameshop.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}

