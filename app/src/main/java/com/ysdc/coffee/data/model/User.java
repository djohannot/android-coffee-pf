package com.ysdc.coffee.data.model;

public class User {
    private final String name;
    private final String ImageUrl;

    public User(String name, String imageUrl) {
        this.name = name;
        ImageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
