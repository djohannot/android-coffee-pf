package com.ysdc.coffee.data.network.mapper;

import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.network.model.Item;
import com.ysdc.coffee.data.network.model.OrderIngredient;
import com.ysdc.coffee.data.network.model.OrderRequest;

public class OrderMapper {

    public OrderMapper() {
        //Nothing to do
    }

    public OrderRequest convertOrder(Order order) {
        OrderRequest orderRequest = new OrderRequest();
        for (OrderedProduct orderedProduct : order.getOrderedProductList()) {
            Item item = new Item();
            item.setId(orderedProduct.getProduct().getId());
            item.setQuantity(orderedProduct.getQuantity());
            item.setNote(orderedProduct.getNote());
            item.setTakeaway(orderedProduct.isTakeaway());
            item.setSize(orderedProduct.getCupSize().getValue());

            for (Ingredient ingredient : orderedProduct.getIngredients()) {
                item.getIngredients().add(new OrderIngredient(ingredient.getId(), 1));
            }
            orderRequest.getItems().add(item);
        }
        return orderRequest;
    }
}
