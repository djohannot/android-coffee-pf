package com.ysdc.coffee.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.ysdc.coffee.R;

import java.util.ArrayList;
import java.util.List;

import static com.ysdc.coffee.utils.AppConstants.EMPTY_STRING;

public class OrderedProduct{
    private final Product product;
    private final Order order;
    private List<Ingredient> ingredients;
    private CupSize cupSize;
    private int quantity;
    private String note;
    private boolean takeaway;
    private Integer sugarQuantity;

    public OrderedProduct(Product product, Order order){
        this.product = product;
        this.order = order;
        quantity = 1;
        cupSize = CupSize.MEDIUM;
        takeaway = false;
        sugarQuantity = 0;
        ingredients = new ArrayList<>();
    }
    public Product getProduct() {
        return product;
    }

    public Order getOrder() {
        return order;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public CupSize getCupSize() {
        return cupSize;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getNote() {
        return note;
    }

    public boolean isTakeaway() {
        return takeaway;
    }

    public Integer getSugarQuantity() {
        return sugarQuantity;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setCupSize(CupSize cupSize) {
        this.cupSize = cupSize;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTakeaway(boolean takeaway) {
        this.takeaway = takeaway;
    }

    public void setSugarQuantity(Integer sugarQuantity) {
        this.sugarQuantity = sugarQuantity;
    }

    public String getOrderDetails(Context context) {
        return context.getString(cupSize.getLocalizableKey()) + ", " +
                sugarQuantity + context.getString(R.string.sugar_stick) + ", " +
                getIngredients().size()  + context.getString(R.string.ingredients) + (takeaway ? ", " + context.getString(R.string.take_away) : EMPTY_STRING);
    }
}
