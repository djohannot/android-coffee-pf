package com.ysdc.coffee.data.model;

public enum Ingredient {
    CARMEL(""), TOFFEENUT(""), VANILLA("");

    private final String id;

    Ingredient(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
