package com.gam_shop.gameshop.services;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.gam_shop.gameshop.entity.Order;
import com.gam_shop.gameshop.entity.Product;
import com.gam_shop.gameshop.entity.User;
import com.gam_shop.gameshop.repository.OrderRepository;
import com.gam_shop.gameshop.repository.ProductRepository;
import com.gam_shop.gameshop.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;

    }

    public void processPurchase(Long buyerId, Long productId, int quantity) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + buyerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock for product with id " + productId);
        }

        double totalCast = product.getPrice() * quantity;

        if (buyer.getBalance() < totalCast) {
            throw new RuntimeException("Not enough balance for user with id " + buyerId);
        }

        buyer.setBalance(buyer.getBalance() - totalCast);
        product.setQuantity(product.getQuantity() - quantity);

        userRepository.save(buyer);
        productRepository.save(product);

        Order order = new Order(buyer, product, quantity, LocalDateTime.now());
        orderRepository.save(order);
    }

    public double calculateIncomeForDay(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endofDay = date.atTime(LocalTime.MAX);

        List<Order> orders = orderRepository.findAllByOrderDateBetween(startOfDay, endofDay);
        return orders.stream()
                .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
                .sum();
    }

    public double calculateIncomeForMonth(int month, int year) {
        LocalDateTime startOfMonth = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.of(year, month, 1).plusMonths(1).atStartOfDay();

        List<Order> orders = orderRepository.findAllByOrderDateBetween(startOfMonth, endOfMonth);
        return orders.stream()
                .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
                .sum();
    }

    public double calculateIncomeForYear(int year) {
        LocalDateTime startOfYear = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endOfYear = LocalDate.of(year, 1, 1).plusYears(1).atStartOfDay();

        List<Order> orders = orderRepository.findAllByOrderDateBetween(startOfYear, endOfYear);
        return orders.stream()
                .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
                .sum();
    }



}

