package com.ysdc.coffee.data.model;

import java.util.List;

public class OrderedProduct {
    private final Product product;
    private final List<Topping> toppings;
    private final CupSize cupSize;
    private final int quantity;
    private final String note;
    private final boolean takeaway;
    private final Integer sugarQuantity;

    public OrderedProduct(Product product, List<Topping> toppings, CupSize cupSize, int quantity, String note, boolean takeaway, Integer sugarQuantity) {
        this.product = product;
        this.toppings = toppings;
        this.cupSize = cupSize;
        this.quantity = quantity;
        this.note = note;
        this.takeaway = takeaway;
        this.sugarQuantity = sugarQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public List<Topping> getToppings() {
        return toppings;
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


    public static class Builder {

        private Product product;
        private List<Topping> toppings;
        private CupSize cupSize;
        private int quantity;
        private String note;
        private boolean takeaway;
        private Integer sugarQuantity;

        public Builder withProduct(Product product) {
            this.product = product;
            return this;
        }

        public Builder withToppings(List<Topping> toppings) {
            this.toppings = toppings;
            return this;
        }

        public Builder withCupSize(CupSize cupSize) {
            this.cupSize = cupSize;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withNote(String note) {
            this.note = note;
            return this;
        }

        public Builder withTakeaway(boolean takeaway) {
            this.takeaway = takeaway;
            return this;
        }

        public Builder withSugarQuantity(Integer sugarQuantity) {
            this.sugarQuantity = sugarQuantity;
            return this;
        }

        public OrderedProduct build() {
            return new OrderedProduct(product, toppings, cupSize, quantity, note, takeaway, sugarQuantity);
        }
    }
}
