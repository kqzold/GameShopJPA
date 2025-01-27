package com.gam_shop.gameshop.interfaces;

import com.gam_shop.gameshop.entity.Order;

public interface OrderHelper extends AppHelper<Order> {
    void processPurchase();
    void calculateIncome();
}
