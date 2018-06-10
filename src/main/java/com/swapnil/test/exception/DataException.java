package com.swapnil.test.exception;

public class DataException extends Exception {

    public DataException(String message){
        super(message);
    }

    public DataException(String message,Exception e){
        super(message,e);
    }
}
