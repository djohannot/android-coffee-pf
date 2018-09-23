package com.ysdc.coffee.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserOrder {
    private String id;
    private User user;
    private Date date;
    private OrderStatus status;
    private List<OrderProducts> orderedProductList;

    public UserOrder() {
        orderedProductList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderEntryList(List<OrderProducts> orderedProductList) {
        this.orderedProductList = orderedProductList;
    }

    public void addEntry(OrderProducts entry) {
        orderedProductList.add(entry);
    }

    public List<OrderProducts> getOrderedProductList() {
        return orderedProductList;
    }
}
