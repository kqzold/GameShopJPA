package com.gam_shop.gameshop.interfaces;

import java.util.List;
import java.util.Optional;

public interface AppHelper<T> {
    Optional<T> create();
    Optional<T> edit(T entity);
    boolean printList(List<T> entities);
    Long findIdEntityForChangeAvailability(List<T> entities);
    List<T> getAllEntities();
}