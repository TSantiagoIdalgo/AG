package com.ancore.ancoregaming.cart.interfaces;

import org.apache.coyote.BadRequestException;

public interface Command<T> {
    T execute() throws BadRequestException;

    T undo() throws BadRequestException;

    T getResult();
}
