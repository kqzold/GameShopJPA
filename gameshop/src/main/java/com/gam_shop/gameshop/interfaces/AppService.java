package com.gam_shop.gameshop.interfaces;

import java.util.List;

public interface AppService<T> {
    void add(T entity);
    void update(Long id, T entity);
    void delete(Long id);
    T getById(Long id);
    List<T> getAll();
}
