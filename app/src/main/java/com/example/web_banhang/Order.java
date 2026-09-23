package com.example.web_banhang;

public class Order {

    private int id;
    private String username;
    private String customerName;
    private String phone;
    private String address;
    private String paymentMethod;
    private double totalMoney;
    private String status;
    private String orderDate;

    public Order() {
    }

    public Order(
            int id,
            String username,
            String customerName,
            String phone,
            String address,
            String paymentMethod,
            double totalMoney,
            String status,
            String orderDate
    ) {
        this.id = id;
        this.username = username;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.totalMoney = totalMoney;
        this.status = status;
        this.orderDate = orderDate;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}