package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Item {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("size")
    private Integer size;
    @SerializedName("sugar")
    private Integer sugarQuantity;
    @SerializedName("take_away")
    private boolean takeaway;
    @SerializedName("description")
    private String description;
    @SerializedName("quantity")
    private Integer quantity;
    @SerializedName("note")
    private String note;
    @SerializedName("image")
    private String imageUrl;
    @SerializedName("ingredients")
    private List<OrderIngredient> ingredients;
    @SerializedName("coffee")
    private Coffee coffee;

    public Item() {
        this.ingredients = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    class Coffee {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("image")
        private String imageUrl;
    }

    public String getCoffeeId(){
        return coffee.id;
    }

    public String getCoffeeName(){
        return coffee.name;
    }

    public String getCoffeeImage(){
        return coffee.imageUrl;
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

    public Integer getQuantity() {
        return quantity;
    }

    public String getNote() {
        return note;
    }

    public Integer getSugarQuantity() {
        return sugarQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<OrderIngredient> getIngredients() {
        return ingredients;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setTakeaway(boolean takeaway) {
        this.takeaway = takeaway;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSugarQuantity(Integer sugarQuantity) {
        this.sugarQuantity = sugarQuantity;
    }

    public boolean hasIngredients() {
        return this.ingredients != null && !ingredients.isEmpty();
    }
}