package com.evoila.springsecuritytask.exception;


public class InvalidRoleException extends RuntimeException{

    private String message;

    public InvalidRoleException(String message) {
        super(message);
    }
}
