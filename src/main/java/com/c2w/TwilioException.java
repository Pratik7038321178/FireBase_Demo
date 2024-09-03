package com.c2w;



public class TwilioException extends Exception {
    public TwilioException(String message) {
        super(message);
    }

    public TwilioException(String message, Throwable cause) {
        super(message, cause);
    }
}

