package com.github.secondbase.core;

/**
 * Exception wrapper for SecondBase exceptions.
 */
public class SecondBaseException extends RuntimeException {
    public SecondBaseException(final String s, final Throwable e) {
        super(s, e);
    }

    public SecondBaseException(final String s) {
        super(s);
    }
}
