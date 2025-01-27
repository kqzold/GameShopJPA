package com.gam_shop.gameshop.apphelper;

import com.gam_shop.gameshop.entity.User;
import com.gam_shop.gameshop.interfaces.AppService;
import com.gam_shop.gameshop.interfaces.UserHelper;
import com.gam_shop.gameshop.interfaces.Input;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;


@Component
public class UserHelperImpl implements UserHelper {

    private final Input input;
    private final AppService<User> userService;

    public UserHelperImpl(Input input, AppService<User> userService) {
        this.input = input;
        this.userService = userService;
    }

    @Override
    public Optional<User> create() {
        try {
            String username = getValidStringInput("Введите имя пользователя:");
            String password = getValidStringInput("Введите пароль пользователя:");
            double balance = getValidDoubleInput("Введите баланс пользователя:");
            User user = new User(username, password, balance);
            userService.add(user);
            System.out.println("Пользователь успешно добавлен.");
            return Optional.of(user);
        } catch (Exception e) {
            System.out.println("Ошибка при создании пользователя: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> edit(User user) {
        try {
            String newName = getValidStringInput("Введите новое имя пользователя:");
            double newBalance = getValidDoubleInput("Введите новый баланс пользователя:");
            String newPassword = getValidStringInput("Введите новый пароль пользователя:");
            user.setUsername(newName);
            user.setBalance(newBalance);
            user.setPassword(newPassword);
            userService.update(user.getId(), user);
            System.out.println("Пользователь успешно обновлён.");
            return Optional.of(user);
        }catch (Exception e) {
            System.out.println("Ошибка при редактировании пользователя: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean printList(List<User> users) {
        try {
            if(users.isEmpty()) {
                System.out.println("Список пользователей пуст.");
                return false;
            }
            System.out.println("Список пользователей:");
            for (User user : users) {
                System.out.printf("ID: %d, Имя: %s, Пароль: %s Баланс: %.2f%n",
                        user.getId(), user.getUsername(), user.getPassword(), user.getBalance());
            }
            return true;
        }catch (Exception e) {
            System.out.println("Ошибка при выводе списка пользователей: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Long findIdEntityForChangeAvailability(List<User> users) {
        this.printList(users);
        System.out.print("Выберите ID покупателя для изменения доступности: ");
        return getValidLongInput();
    }

    @Override
    public List<User> getAllEntities() {
        return userService.getAll();
    }

    @Override
    public void listUsers() {
        List<User> users = getAllEntities();
        printList(users);
    }

    @Override
    public void updateUser() {
        List<User> users = getAllEntities();
        Long id = findIdEntityForChangeAvailability(users);
        User user = userService.getById(id);
        if (user != null) {
            edit(user);
        } else {
            System.out.println("Покупатель с таким ID не найден.");
        }
    }

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