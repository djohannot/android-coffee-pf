package com.ysdc.coffee.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Order {
    private String id;
    private User user;
    private Date date;
    private OrderStatus status;
    private List<OrderEntry> entries;

    public Order(){
        entries = new ArrayList<>();
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

    public void addEntry(OrderEntry entry) {
        entries.add(entry);
    }

    public void removeEntry(OrderEntry entry){
        entries.remove(entry);
    }

    public List<OrderEntry> getEntries() {
        return entries;
    }
}
