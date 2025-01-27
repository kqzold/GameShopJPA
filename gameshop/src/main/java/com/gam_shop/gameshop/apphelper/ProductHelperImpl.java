package com.gam_shop.gameshop.apphelper;

import com.gam_shop.gameshop.entity.Product;
import com.gam_shop.gameshop.interfaces.ProductHelper;
import com.gam_shop.gameshop.interfaces.Input;
import com.gam_shop.gameshop.interfaces.AppService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductHelperImpl implements ProductHelper {

    private final Input input;
    private final AppService<Product> productService;

    public ProductHelperImpl(Input input, AppService<Product> productService) {
        this.input = input;
        this.productService = productService;
    }

    @Override
    public Optional<Product> create() {
        try {
            String name = getValidStringInput("Введите название продукта:");
            double price = getValidDoubleInput("Введите цену продукта:");
            int quantity = getValidIntInput("Введите количество продукта:");
            Product product = new Product(name, price, quantity);
            productService.add(product);
            System.out.println("Продукт успешно добавлен.");
            return Optional.of(product);
        } catch (Exception e) {
            System.out.println("Ошибка при создании продукта: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Product> edit(Product product) {
        try {
            String newName = getValidStringInput("Введите новое название продукта:");
            double newPrice = getValidDoubleInput("Введите новую цену продукта:");
            int newQuantity = getValidIntInput("Введите новое количество продукта:");
            product.setName(newName);
            product.setPrice(newPrice);
            product.setQuantity(newQuantity);
            productService.update(product.getId(), product);
            System.out.println("Продукт успешно обновлён.");
            return Optional.of(product);
        } catch (Exception e) {
            System.out.println("Ошибка при редактировании продукта: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean printList(List<Product> products) {
        try {
            if (products.isEmpty()) {
                System.out.println("Список продуктов пуст");
                return false;
            }
            System.out.println("---------- Список продуктов --------");
            for (Product product : products) {
                System.out.printf("ID: %d, Название: %s, Цена: %.2f, Количество: %d%n",
                        product.getId(), product.getName(), product.getPrice(), product.getQuantity());
            }
            return true;
        } catch (Exception e) {
            System.out.println("Ошибка при выводе списка продуктов: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Long findIdEntityForChangeAvailability(List<Product> products) {
        this.printList(products);
        System.out.print("Выберите ID продукта для изменения доступности: ");
        return getValidLongInput();
    }

    @Override
    public List<Product> getAllEntities() {
        return productService.getAll();
    }

    @Override
    public void listProducts() {
        List<Product> products = getAllEntities();
        printList(products);
    }

    @Override
    public void updateProduct() {
        List<Product> products = getAllEntities();
        Long id = findIdEntityForChangeAvailability(products);
        Product product = productService.getById(id);
        if (product != null) {
            edit(product);
        } else {
            System.out.println("Продукт с таким ID не найден.");
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    // Вспомогательные методы для проверки ввода
    private String getValidStringInput(String prompt) {
        System.out.println(prompt);
        return input.getString().trim();
    }

    private double getValidDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.println(prompt);
                return input.getDouble();
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число.");
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

    private Long getValidLongInput() {
        while (true) {
            try {
                return input.getLong();
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное целое число.");
            }
        }
    }
}