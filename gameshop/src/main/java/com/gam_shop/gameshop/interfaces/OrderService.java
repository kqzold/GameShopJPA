package com.gam_shop.gameshop.interfaces;

import com.gam_shop.gameshop.entity.Order;

import java.time.LocalDate;

public interface OrderService extends AppService<Order> {
    void processPurchase(Long buyerId, Long productId, int quantity);
    double calculateIncomeForDay(LocalDate date);
    double calculateIncomeForMonth(int year, int month);
    double calculateIncomeForYear(int year);
}
