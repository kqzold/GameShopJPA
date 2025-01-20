package com.gam_shop.gameshop.controller;

import com.gam_shop.gameshop.entity.Order;
import com.gam_shop.gameshop.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;



@RestController
@RequestMapping({"/orders"})
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        this.orderService.processPurchase(userId, productId, quantity);
        return new Order();
    }

    @GetMapping({"/income/daily"})
    public double getDailyIncome(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return this.orderService.calculateIncomeForDay(localDate);
    }

    @GetMapping({"/income/monthly"})
    public double getMonthlyIncome(@RequestParam int month, @RequestParam int year) {
        return this.orderService.calculateIncomeForMonth(month, year);
    }

    @GetMapping({"/income/yearly"})
    public double getYearlyIncome(@RequestParam int year) {
        return this.orderService.calculateIncomeForYear(year);
    }
}
