package com.example.web_banhang.api;

import java.util.List;

public class OrderApi {

    private int id;
    private int userId;
    private String username;
    private String customerName;
    private String phone;
    private String address;
    private String paymentMethod;
    private double totalMoney;
    private String status;
    private String orderDate;
    private List<OrderItemApi> items;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public List<OrderItemApi> getItems() {
        return items;
    }
}