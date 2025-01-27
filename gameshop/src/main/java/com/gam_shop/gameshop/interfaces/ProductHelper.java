package com.gam_shop.gameshop.interfaces;

import com.gam_shop.gameshop.entity.Product;

import java.util.List;

public interface ProductHelper extends AppHelper<Product> {
    void listProducts();
    void updateProduct();
    List<Product> getAllProducts();
}
