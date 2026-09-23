package com.example.web_banhang;

public class CartItem {

    private int id;
    private String username;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String imageUri;

    public CartItem() {
    }

    public CartItem(
            int id,
            String username,
            int productId,
            String productName,
            double price,
            int quantity,
            String imageUri
    ) {
        this.id = id;
        this.username = username;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public double getTotalPrice() {
        return price * quantity;
    }
}