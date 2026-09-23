package com.example.web_banhang;

public class Product {

    private int id;
    private String name;
    private double price;
    private String description;
    private int stock;

    // Đường dẫn ảnh
    private String imageUri;

    public Product() {
    }

    // Constructor cũ
    public Product(
            int id,
            String name,
            double price,
            String description,
            int stock
    ) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    // Constructor đầy đủ
    public Product(
            int id,
            String name,
            double price,
            String description,
            int stock,
            String imageUri
    ) {

        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.imageUri = imageUri;
    }

    // Constructor thêm sản phẩm
    public Product(
            String name,
            double price,
            String description,
            int stock
    ) {

        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    // Constructor thêm sản phẩm có ảnh
    public Product(
            String name,
            double price,
            String description,
            int stock,
            String imageUri
    ) {

        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.imageUri = imageUri;
    }

    // =========================
    // GET / SET
    // =========================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}