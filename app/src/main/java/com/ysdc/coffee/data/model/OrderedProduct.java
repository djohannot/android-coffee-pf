package com.ysdc.coffee.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrderedProduct implements Parcelable {
    private final Product product;
    private List<Ingredient> ingredients;
    private CupSize cupSize;
    private int quantity;
    private String note;
    private boolean takeaway;
    private Integer sugarQuantity;

    public OrderedProduct(Product product){
        this.product = product;
        quantity = 1;
        cupSize = CupSize.MEDIUM;
        takeaway = false;
        sugarQuantity = 0;
        ingredients = new ArrayList<>();
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

    protected OrderedProduct(Parcel in) {
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

    public static final Parcelable.Creator<OrderedProduct> CREATOR = new Parcelable.Creator<OrderedProduct>() {
        @Override
        public OrderedProduct createFromParcel(Parcel source) {
            return new OrderedProduct(source);
        }

        @Override
        public OrderedProduct[] newArray(int size) {
            return new OrderedProduct[size];
        }
    };
}
