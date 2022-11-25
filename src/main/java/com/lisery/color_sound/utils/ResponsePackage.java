package com.lisery.color_sound.utils;

public class ResponsePackage<T> {
    public ResponsePackage(ResponseStatusCode status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseStatusCode getStatus() {
        return status;
    }

    public void setStatus(ResponseStatusCode status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private ResponseStatusCode status;
    private String message;
    private T data;
}
