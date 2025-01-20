package com.gam_shop.gameshop.services;

import org.springframework.stereotype.Service;
import com.gam_shop.gameshop.entity.Product;
import com.gam_shop.gameshop.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingproduct -> {
                    existingproduct.setName(updatedProduct.getName());
                    existingproduct.setPrice(updatedProduct.getPrice());
                    existingproduct.setQuantity(updatedProduct.getQuantity());
                    return productRepository.save(existingproduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
