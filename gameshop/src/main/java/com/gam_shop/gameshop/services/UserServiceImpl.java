package com.gam_shop.gameshop.services;

import org.springframework.stereotype.Service;
import com.gam_shop.gameshop.entity.User;
import com.gam_shop.gameshop.interfaces.AppService;
import com.gam_shop.gameshop.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements AppService<User> {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(Long id, User updatedUser) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setBalance(updatedUser.getBalance());
            userRepository.save(user);
        }, () -> {
            throw new RuntimeException("Пользователь не найден с ID: " + id);
        });
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден с ID: " + id));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
