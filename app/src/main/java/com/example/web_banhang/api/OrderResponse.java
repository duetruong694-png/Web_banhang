package com.example.web_banhang.api;

import java.util.List;

public class OrderResponse {

    private boolean success;
    private String message;
    private List<OrderApi> orders;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<OrderApi> getOrders() {
        return orders;
    }
}