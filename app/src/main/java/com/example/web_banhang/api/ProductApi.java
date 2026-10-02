package com.example.web_banhang.api;

public class ProductApi {

    private int id;
    private String productCode;
    private String name;
    private double price;
    private String description;
    private int stock;
    private String imageUri;
    private String saleDate;
    private String status;

    public int getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public String getStatus() {
        return status;
    }
}