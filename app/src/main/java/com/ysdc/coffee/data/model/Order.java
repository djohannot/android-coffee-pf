package com.ysdc.coffee.data.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<OrderedProduct> orderedProductList;

    public Order(){
        orderedProductList = new ArrayList<>();
    }

    public void addOrderedProduct(OrderedProduct orderedProduct) {
        orderedProductList.add(orderedProduct);
    }

    public List<OrderedProduct> getOrderedProductList() {
        return orderedProductList;
    }
}
