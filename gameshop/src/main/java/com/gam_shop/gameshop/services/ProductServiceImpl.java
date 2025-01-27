package com.gam_shop.gameshop.services;

import org.springframework.stereotype.Service;
import com.gam_shop.gameshop.entity.Product;
import com.gam_shop.gameshop.repository.ProductRepository;
import com.gam_shop.gameshop.interfaces.AppService;

import java.util.List;

@Service
public class ProductServiceImpl implements AppService<Product>{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void add(Product product) {
        productRepository.save(product);
    }

    @Override
    public void update(Long id, Product updatedProduct) {
        productRepository.findById(id).ifPresentOrElse(product -> {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            productRepository.save(product);
        }, () -> {
            throw new RuntimeException("Продукт не найден с ID: " + id);
        });
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден с ID: " + id));
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }
}
