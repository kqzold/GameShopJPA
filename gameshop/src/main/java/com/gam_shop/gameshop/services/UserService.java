package com.gam_shop.gameshop.services;

import org.springframework.stereotype.Service;
import com.gam_shop.gameshop.entity.User;
import com.gam_shop.gameshop.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, User updatedUser) {
        userRepository.findById(id).map(existingUser -> {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setPassword(updatedUser.getPassword());
                    existingUser.setBalance(updatedUser.getBalance());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

}
