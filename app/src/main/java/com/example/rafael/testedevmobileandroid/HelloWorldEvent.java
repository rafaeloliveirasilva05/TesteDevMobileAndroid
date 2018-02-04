package com.example.rafael.testedevmobileandroid;

/**
 * Created by rafael on 30/01/18.
 */

public class HelloWorldEvent {
    private final String message;


    public HelloWorldEvent(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
