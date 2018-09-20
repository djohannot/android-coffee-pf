package com.ysdc.coffee.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private User user;
    private Date date;
    private OrderStatus status;
    private List<OrderEntry> orderedProductList;

    public Order(){
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

    public void setOrderEntryList(List<OrderEntry> orderedProductList) {
        this.orderedProductList = orderedProductList;
    }

    public void addEntry(OrderEntry entry) {
        orderedProductList.add(entry);
    }

    public List<OrderEntry> getOrderedProductList() {
        return orderedProductList;
    }
}
