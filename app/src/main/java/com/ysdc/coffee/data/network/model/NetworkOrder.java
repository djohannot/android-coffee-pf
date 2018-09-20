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

    public String getUserName() {
        return user.name;
    }

    public String getUserEmail() {
        return user.email;
    }

    public String getImageUrl() {
        return user.imageUrl;
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

}
