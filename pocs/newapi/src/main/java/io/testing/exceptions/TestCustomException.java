package io.testing.exceptions;
//Custom Exception creation as per need your application
public class TestCustomException extends  RuntimeException{
    public TestCustomException(String message) {
        super(message);
    }
}
