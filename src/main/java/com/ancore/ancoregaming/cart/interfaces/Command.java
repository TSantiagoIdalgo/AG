package com.ancore.ancoregaming.cart.interfaces;

public interface Command<T> {
    T execute();

    T undo();

    T getResult();
}
