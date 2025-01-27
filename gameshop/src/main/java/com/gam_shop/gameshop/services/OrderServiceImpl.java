package com.gam_shop.gameshop.services;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.gam_shop.gameshop.entity.Order;
import com.gam_shop.gameshop.entity.Product;
import com.gam_shop.gameshop.entity.User;
import com.gam_shop.gameshop.repository.OrderRepository;
import com.gam_shop.gameshop.repository.ProductRepository;
import com.gam_shop.gameshop.repository.UserRepository;
import com.gam_shop.gameshop.interfaces.OrderService;
import org.springframework.transaction.annotation.Transactional;



import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;


@Service
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(UserRepository userRepository,
                            ProductRepository productRepository,
                            OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void processPurchase(Long buyerId, Long productId, int quantity) {
        User user = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Покупатель не найден с ID: " + buyerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Продукт не найден с ID: " + productId));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Недостаточно товара на складе.");
        }

        double totalPrice = product.getPrice() * quantity;
        if (user.getBalance() < totalPrice) {
            throw new RuntimeException("Недостаточно средств у покупателя.");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        user.setBalance(user.getBalance() - totalPrice);
        userRepository.save(user);

        Order order = new Order(user, product, quantity, LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public double calculateIncomeForDay(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return orderRepository.findAllByOrderDateBetween(start, end)
                .stream()
                .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
                .sum();
    }

    @Override
    public double calculateIncomeForMonth(int year, int month) {
        LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);
        return orderRepository.findAllByOrderDateBetween(start, end)
                .stream()
                .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
                .sum();
    }

    @Override
    public double calculateIncomeForYear(int year) {
        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).atTime(LocalTime.MAX);
        return orderRepository.findAllByOrderDateBetween(start, end)
                .stream()
                .mapToDouble(order -> order.getProduct().getPrice() * order.getQuantity())
                .sum();
    }

    @Override
    public void add(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void update(Long id, Order updatedOrder) {
        orderRepository.findById(id).ifPresentOrElse(order -> {
            order.setUser(updatedOrder.getUser());
            order.setProduct(updatedOrder.getProduct());
            order.setQuantity(updatedOrder.getQuantity());
            order.setOrderDate(updatedOrder.getOrderDate());
            orderRepository.save(order);
        }, () -> {
            throw new RuntimeException("Заказ не найден с ID: " + id);
        });
    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден с ID: " + id));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
}
