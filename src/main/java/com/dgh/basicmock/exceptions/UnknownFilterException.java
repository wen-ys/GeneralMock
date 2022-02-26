package com.dgh.basicmock.exceptions;

public class UnknownFilterException extends RuntimeException{
    public UnknownFilterException(String type) {
        super(type + " is unknown filter.");
    }
}
