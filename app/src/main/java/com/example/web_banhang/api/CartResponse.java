package com.example.web_banhang.api;

import java.util.List;

public class CartResponse {

    private boolean success;
    private String message;
    private List<CartItemApi> items;
    private double total;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<CartItemApi> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }
}