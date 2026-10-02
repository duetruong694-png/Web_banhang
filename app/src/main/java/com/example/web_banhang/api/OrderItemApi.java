package com.example.web_banhang.api;

public class OrderItemApi {

    private int id;
    private int productId;
    private String productName;
    private double price;
    private int quantity;

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}