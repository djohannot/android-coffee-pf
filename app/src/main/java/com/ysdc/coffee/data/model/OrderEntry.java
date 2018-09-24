package com.ysdc.coffee.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.ysdc.coffee.R;

import java.util.ArrayList;
import java.util.List;

import static com.ysdc.coffee.utils.AppConstants.EMPTY_STRING;

public class OrderEntry implements Parcelable {
    private List<Ingredient> ingredients;
    private CupSize cupSize;
    private int quantity;
    private String note;
    private boolean takeaway;
    private Integer sugarQuantity;
    private String coffeeName;
    private String coffeeId;
    private String coffeeImageUrl;

    public OrderEntry(){
        quantity = 1;
        cupSize = CupSize.MEDIUM;
        takeaway = false;
        sugarQuantity = 0;
        ingredients = new ArrayList<>();
    }

    public String getCoffeeName() {
        return coffeeName;
    }

    public String getCoffeeId() {
        return coffeeId;
    }

    public String getCoffeeImageUrl() {
        return coffeeImageUrl;
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
                sugarQuantity + " " + context.getString(R.string.sugar_stick) + ", " +
                getIngredients().size() + " " + context.getString(R.string.ingredients) + (takeaway ? ", " + context.getString(R.string.take_away) : EMPTY_STRING);
    }

    public void setCoffeeName(String coffeeName) {
        this.coffeeName = coffeeName;
    }

    public void setCoffeeId(String coffeeId) {
        this.coffeeId = coffeeId;
    }

    public void setCoffeeImageUrl(String coffeeImageUrl) {
        this.coffeeImageUrl = coffeeImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.ingredients);
        dest.writeInt(this.cupSize == null ? -1 : this.cupSize.ordinal());
        dest.writeInt(this.quantity);
        dest.writeString(this.note);
        dest.writeByte(this.takeaway ? (byte) 1 : (byte) 0);
        dest.writeValue(this.sugarQuantity);
        dest.writeString(this.coffeeName);
        dest.writeString(this.coffeeId);
        dest.writeString(this.coffeeImageUrl);
    }

    protected OrderEntry(Parcel in) {
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        int tmpCupSize = in.readInt();
        this.cupSize = tmpCupSize == -1 ? null : CupSize.values()[tmpCupSize];
        this.quantity = in.readInt();
        this.note = in.readString();
        this.takeaway = in.readByte() != 0;
        this.sugarQuantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.coffeeName = in.readString();
        this.coffeeId = in.readString();
        this.coffeeImageUrl = in.readString();
    }

    public static final Parcelable.Creator<OrderEntry> CREATOR = new Parcelable.Creator<OrderEntry>() {
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
