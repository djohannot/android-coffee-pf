package com.ysdc.coffee.ui.create;

import android.os.Parcelable;

import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.ui.base.MvpPresenter;

import io.reactivex.Completable;

public interface CreateOrderMvpPresenter<V extends CreateOrderMvpView> extends MvpPresenter<V> {

    Product getProduct();

    void incrementQuantity();

    void decrementQuantity();

    void setSizeSelected(CupSize large);

    void setSugarSelected(int quantity);

    void carmelSelected(boolean b);

    void toffeenutSelected(boolean b);

    void vanillaSelected(boolean b);

    void isTakeAway(boolean b);

    Completable addOrder();

    void setOrderProduct(OrderedProduct orderProduct);
}
