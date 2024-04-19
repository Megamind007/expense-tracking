package com.example.minispring.validation;

public class NotFound extends RuntimeException{
    public NotFound(String message){
        super(message);
    }
}
