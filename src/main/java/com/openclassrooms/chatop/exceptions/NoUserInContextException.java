package com.openclassrooms.chatop.exceptions;

public class NoUserInContextException extends Exception{
    public NoUserInContextException(String message) {
        super(message);
    }
}