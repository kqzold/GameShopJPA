package com.gam_shop.gameshop.interfaces;

import com.gam_shop.gameshop.entity.User;


public interface UserHelper extends AppHelper<User> {
    void listUsers();
    void updateUser();
}