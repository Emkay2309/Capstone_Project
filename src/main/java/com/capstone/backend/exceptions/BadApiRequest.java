package com.capstone.backend.exceptions;

public class BadApiRequest extends  RuntimeException{
    public BadApiRequest(String message) {
        super(message);
    }

    public BadApiRequest() {
        super("Bad Api requested...!!!");
    }
}
