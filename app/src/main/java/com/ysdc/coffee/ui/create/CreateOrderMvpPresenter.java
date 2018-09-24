package com.ysdc.coffee.ui.create;

import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.ui.base.MvpPresenter;

import java.util.List;

import io.reactivex.Completable;

public interface CreateOrderMvpPresenter<V extends CreateOrderMvpView> extends MvpPresenter<V> {

    OrderEntry getOrderEntry();

    void incrementQuantity();

    void decrementQuantity();

    void setSizeSelected(CupSize cupSize);

    void setSugarSelected(int quantity);

    void ingredientModified(String ingredientId, boolean selected);

    void isTakeAway(boolean b);

    Completable addOrder();

    void setOrderEntry(OrderEntry entry);

    List<Ingredient> getIngredients();

    boolean getSwitchValueForIngredient(Ingredient ingredient);
}
