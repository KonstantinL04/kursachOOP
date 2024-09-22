package com.example.kursachoop.Model;

public class Order {

    private String orderID, userName, userPhone, date, time, status;
    private int totalPrice;

    public Order() {
    }

    public Order(String orderID, String userName, String userPhone, int totalPrice, String date, String time, String status) {
        this.orderID = orderID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
