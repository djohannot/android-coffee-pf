package com.ysdc.coffee.data.model;

public enum Ingredient {
    CARMEL("fabf8ea0-1d62-4e5c-9f3d-b324f751a7a0"), TOFFEENUT("b07892e2-b740-4315-a31c-dbcbc57d887f"), VANILLA("70db6703-6922-40e4-ad2f-ec583b2d54c5");

    private final String id;

    Ingredient(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
