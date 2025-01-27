package com.gam_shop.gameshop.apphelper;

import com.gam_shop.gameshop.entity.Order;
import com.gam_shop.gameshop.interfaces.AppService;
import com.gam_shop.gameshop.interfaces.OrderService;
import com.gam_shop.gameshop.interfaces.Input;
import com.gam_shop.gameshop.interfaces.OrderHelper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;
import java.time.LocalDate;


@Component
public class OrderHelperImpl implements OrderHelper {

    private final Input input;
    private final AppService<Order> orderService;
    private final OrderService specificOrderService; // Специфичные сервисы для заказов

    public OrderHelperImpl(Input input, AppService<Order> orderService, OrderService specificOrderService) {
        this.input = input;
        this.orderService = orderService;
        this.specificOrderService = specificOrderService;
    }

    @Override
    public Optional<Order> create() {
        return Optional.empty();
    }

    @Override
    public Optional<Order> edit(Order order) {
        return Optional.empty();
    }

    @Override
    public boolean printList(List<Order> orders) {
        try {
            if (orders.isEmpty()) {
                System.out.println("Список заказов пуст");
                return false;
            }
            System.out.println("---------- Список заказов --------");
            for (Order order : orders) {
                System.out.printf("ID: %d, Покупатель: %s, Продукт: %s, Количество: %d, Дата: %s%n",
                        order.getId(),
                        order.getUser().getUsername(),
                        order.getProduct().getName(),
                        order.getQuantity(),
                        order.getOrderDate());
            }
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при выводе списка заказов: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Long findIdEntityForChangeAvailability(List<Order> orders) {
        this.printList(orders);
        System.out.print("Выберите ID заказа для изменения доступности: ");
        return getValidLongInput("Введите ID заказа:");
    }

    @Override
    public List<Order> getAllEntities() {
        return orderService.getAll();
    }

    @Override
    public void processPurchase() {
        try {
            Long buyerId = getValidLongInput("Введите ID покупателя:");
            Long productId = getValidLongInput("Введите ID продукта:");
            int quantity = getValidIntInput("Введите количество продукта:");
            specificOrderService.processPurchase(buyerId, productId, quantity);
            System.out.println("Покупка успешно завершена.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка при выполнении покупки: " + e.getMessage());
        }
    }

    @Override
    public void calculateIncome() {
        System.out.println("Выберите период:");
        System.out.println("1. День");
        System.out.println("2. Месяц");
        System.out.println("3. Год");

        int choice = getValidIntInput("Введите номер периода:");

        try {
            switch (choice) {
                case 1 -> handleDailyIncome();
                case 2 -> handleMonthlyIncome();
                case 3 -> handleYearlyIncome();
                default -> System.out.println("Неверный выбор.");
            }
        } catch (RuntimeException e) {
            System.out.println("Ошибка при расчёте дохода: " + e.getMessage());
        }
    }

    private void handleDailyIncome() {
        LocalDate date = getValidDateInput("Введите дату (ГГГГ-ММ-ДД):");
        double income = specificOrderService.calculateIncomeForDay(date);
        System.out.printf("Доход за %s: %.2f%n", date, income);
    }

    private void handleMonthlyIncome() {
        int year = getValidIntInput("Введите год:");
        int month = getValidIntInput("Введите месяц:");
        double income = specificOrderService.calculateIncomeForMonth(year, month);
        System.out.printf("Доход за %d-%02d: %.2f%n", year, month, income);
    }

    private void handleYearlyIncome() {
        int year = getValidIntInput("Введите год:");
        double income = specificOrderService.calculateIncomeForYear(year);
        System.out.printf("Доход за %d: %.2f%n", year, income);
    }

    // Вспомогательные методы для проверки ввода
    private Long getValidLongInput(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return input.getLong();
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное целое число.");
            }
        }
    }

    private int getValidIntInput(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return input.getInt();
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное целое число.");
            }
        }
    }

    private LocalDate getValidDateInput(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return LocalDate.parse(input.getString());
            } catch (Exception e) {
                System.out.println("Введите корректную дату в формате ГГГГ-ММ-ДД.");
            }
        }
    }
}