package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRequest {
    @SerializedName("destination_id")
    private String destinationId;
    @SerializedName("items")
    private List<Item> items;

    public OrderRequest(String destinationId) {
        this.destinationId = destinationId;
        this.items = new ArrayList<>();
    }

    class Item {
        @SerializedName("coffee_id")
        private String id;
        @SerializedName("quantity")
        private Integer quantity;
        @SerializedName("note")
        private String note;
        @SerializedName("take_away")
        private boolean takeaway;
        @SerializedName("size")
        private Integer size;
        @SerializedName("ingredients")
        private List<OrderIngredient> ingredients;

        public Item(String productId, int size, boolean takeaway, String note, Map<String, Integer> orderIngredients) {
            this.id = productId;
            this.size = size;
            this.takeaway = takeaway;
            this.note = note;
            this.ingredients = new ArrayList<>();
            for (String key : orderIngredients.keySet()) {
                ingredients.add(new OrderIngredient(key, orderIngredients.get(key)));
            }
        }
    }

    class OrderIngredient {
        @SerializedName("ingredient_id")
        private String productId;
        @SerializedName("quantity")
        private Integer quantity;

        public OrderIngredient(String productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }

}
