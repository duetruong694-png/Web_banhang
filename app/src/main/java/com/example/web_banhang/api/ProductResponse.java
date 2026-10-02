package com.example.web_banhang.api;

import java.util.List;

public class ProductResponse {

    private boolean success;
    private String message;
    private List<ProductApi> products;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<ProductApi> getProducts() {
        return products;
    }
}