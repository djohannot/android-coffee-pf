package com.ysdc.coffee.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrderEntry implements Parcelable {
    private final Product product;
    private List<Ingredient> ingredients;
    private CupSize cupSize;
    private int quantity;
    private String note;
    private boolean takeaway;
    private Integer sugarQuantity;

    public OrderEntry(Product product) {
        this.product = product;
        quantity = 1;
        cupSize = CupSize.MEDIUM;
        takeaway = false;
        sugarQuantity = 0;
        ingredients = new ArrayList<>();
    }

    public OrderEntry(OrderedProduct orderedProduct) {
        this.product = orderedProduct.getProduct();
        this.quantity = orderedProduct.getQuantity();
        this.cupSize = orderedProduct.getCupSize();
        this.note = orderedProduct.getNote();
        this.takeaway = orderedProduct.isTakeaway();
        this.sugarQuantity = orderedProduct.getSugarQuantity();
        ingredients = new ArrayList<>();
        for (Ingredient ingredient : orderedProduct.getIngredients()) {
            ingredients.add(ingredient);
        }
    }

    public Product getProduct() {
        return product;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.product, flags);
        dest.writeList(this.ingredients);
        dest.writeInt(this.cupSize == null ? -1 : this.cupSize.ordinal());
        dest.writeInt(this.quantity);
        dest.writeString(this.note);
        dest.writeByte(this.takeaway ? (byte) 1 : (byte) 0);
        dest.writeValue(this.sugarQuantity);
    }

    protected OrderEntry(Parcel in) {
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        int tmpCupSize = in.readInt();
        this.cupSize = tmpCupSize == -1 ? null : CupSize.values()[tmpCupSize];
        this.quantity = in.readInt();
        this.note = in.readString();
        this.takeaway = in.readByte() != 0;
        this.sugarQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<OrderEntry> CREATOR = new Creator<OrderEntry>() {
        @Override
        public OrderEntry createFromParcel(Parcel source) {
            return new OrderEntry(source);
        }

        @Override
        public OrderEntry[] newArray(int size) {
            return new OrderEntry[size];
        }
    };
}