package com.example.web_banhang.api;

public class CartItemApi {

    private int id;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private double subtotal;
    private int stock;
    private String imageUri;

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

    public double getSubtotal() {
        return subtotal;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUri() {
        return imageUri;
    }
}