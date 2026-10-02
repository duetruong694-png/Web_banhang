package com.example.web_banhang.api;

public class OrderDetailResponse {

    private boolean success;
    private String message;
    private OrderApi order;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public OrderApi getOrder() {
        return order;
    }
}