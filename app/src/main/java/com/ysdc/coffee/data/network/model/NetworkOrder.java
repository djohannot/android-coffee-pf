package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;
import com.ysdc.coffee.data.model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class NetworkOrder {
    @SerializedName("id")
    private String id;
    @SerializedName("user")
    private User user;
    @SerializedName("destination")
    private Destination destination;
    @SerializedName("date")
    private String date;
    @SerializedName("status")
    private OrderStatus orderStatus;
    @SerializedName("prioritized")
    private boolean prioritized;
    @SerializedName("items")
    private List<Item> items;

    public NetworkOrder() {
        this.items = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Destination getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public boolean isPrioritized() {
        return prioritized;
    }

    public List<Item> getItems() {
        return items;
    }

    class Item {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("size")
        private Integer size;
        @SerializedName("take_away")
        private boolean takeaway;
        @SerializedName("description")
        private String description;
        @SerializedName("quantity")
        private String quantity;
        @SerializedName("note")
        private String note;
        @SerializedName("image")
        private String imageUrl;
        @SerializedName("ingredients")
        private List<OrderIngredient> ingredients;

        public Item() {
            this.ingredients = new ArrayList<>();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getSize() {
            return size;
        }

        public boolean isTakeaway() {
            return takeaway;
        }

        public String getDescription() {
            return description;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getNote() {
            return note;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public List<OrderIngredient> getIngredients() {
            return ingredients;
        }
    }

    class User {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("image")
        private String imageUrl;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    class Destination {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("description")
        private String description;

        public Destination(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
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

        public String getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }

}
