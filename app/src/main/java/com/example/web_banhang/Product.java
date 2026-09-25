package com.example.web_banhang;

public class Product {

    private int id;
    private String productCode;
    private String name;
    private double price;
    private String description;
    private int stock;
    private String imageUri;
    private String saleDate;
    private String status;

    // =====================================================
    // CONSTRUCTOR RỖNG
    // =====================================================

    public Product() {
    }

    // =====================================================
    // CONSTRUCTOR CŨ
    // Giữ lại để các Activity cũ không bị lỗi
    // =====================================================

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
        this.productCode = "";
        this.imageUri = "";
        this.saleDate = "";
        this.status = "Đang bán";
    }

    // =====================================================
    // CONSTRUCTOR CŨ CÓ ẢNH
    // =====================================================

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
        this.productCode = "";
        this.saleDate = "";
        this.status = "Đang bán";
    }

    // =====================================================
    // CONSTRUCTOR THÊM SẢN PHẨM CŨ
    // =====================================================

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
        this.productCode = "";
        this.imageUri = "";
        this.saleDate = "";
        this.status = "Đang bán";
    }

    // =====================================================
    // CONSTRUCTOR THÊM SẢN PHẨM CŨ CÓ ẢNH
    // =====================================================

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
        this.productCode = "";
        this.saleDate = "";
        this.status = "Đang bán";
    }

    // =====================================================
    // CONSTRUCTOR MỚI ĐẦY ĐỦ
    // =====================================================

    public Product(
            String productCode,
            String name,
            double price,
            String description,
            int stock,
            String imageUri,
            String saleDate,
            String status
    ) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.imageUri = imageUri;
        this.saleDate = saleDate;
        this.status = status;
    }

    // =====================================================
    // CONSTRUCTOR MỚI ĐẦY ĐỦ CÓ ID
    // =====================================================

    public Product(
            int id,
            String productCode,
            String name,
            double price,
            String description,
            int stock,
            String imageUri,
            String saleDate,
            String status
    ) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.imageUri = imageUri;
        this.saleDate = saleDate;
        this.status = status;
    }

    // =====================================================
    // GETTER / SETTER
    // =====================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}