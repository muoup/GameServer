package com.Game.Util.Exceptions;

public class ListOutOfBounds extends Exception {
    public ListOutOfBounds() {
        super();
    }

    public ListOutOfBounds(String message) {
        super(message);
    }

    public ListOutOfBounds(Throwable cause) {
        super(cause);
    }

    public ListOutOfBounds(String message, Throwable cause) {
        super(message, cause);
    }
}
