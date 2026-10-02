package com.example.web_banhang.api;

public class CreateOrderResponse {

    private boolean success;
    private String message;
    private int orderId;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getOrderId() {
        return orderId;
    }
}