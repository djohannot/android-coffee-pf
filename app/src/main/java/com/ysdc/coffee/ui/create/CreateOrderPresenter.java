package com.ysdc.coffee.ui.create;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import io.reactivex.Completable;

public class CreateOrderPresenter<V extends CreateOrderMvpView> extends BasePresenter<V> implements CreateOrderMvpPresenter<V> {

    private OrderEntry entry;
    private final OrderRepository orderRepository;

    public CreateOrderPresenter(ErrorHandler errorHandler, OrderRepository orderRepository) {
        super(errorHandler);
        this.orderRepository = orderRepository;
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        load();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void load() {
    }


    @Override
    public Product getProduct() {
        return entry.getProduct();
    }

    @Override
    public void incrementQuantity() {
        entry.setQuantity(entry.getQuantity()+1);
        getMvpView().updateQuantity(entry.getQuantity());
    }

    @Override
    public void decrementQuantity() {
        if(entry.getQuantity()>0) {
            entry.setQuantity(entry.getQuantity() - 1);
            getMvpView().updateQuantity(entry.getQuantity());
        }
    }

    @Override
    public void setSizeSelected(CupSize cupSize) {
        entry.setCupSize(cupSize);
    }

    @Override
    public void setSugarSelected(int quantity) {
        entry.setSugarQuantity(quantity);
    }

    @Override
    public void carmelSelected(boolean selected) {
        if (selected){
            entry.getIngredients().add(Ingredient.CARMEL);
        }else{
            entry.getIngredients().remove(Ingredient.CARMEL);
        }
    }

    @Override
    public void toffeenutSelected(boolean selected) {
        if (selected){
            entry.getIngredients().add(Ingredient.TOFFEENUT);
        }else{
            entry.getIngredients().remove(Ingredient.TOFFEENUT);
        }
    }

    @Override
    public void vanillaSelected(boolean selected) {
        if (selected){
            entry.getIngredients().add(Ingredient.VANILLA);
        }else{
            entry.getIngredients().remove(Ingredient.VANILLA);
        }
    }

    @Override
    public void isTakeAway(boolean isTakeAway) {
        entry.setTakeaway(isTakeAway);
    }

    @Override
    public Completable addOrder() {
        return Completable.defer(() -> {
            orderRepository.addProductToOrder(entry);
            return Completable.complete();
        });
    }

    @Override
    public void setOrderEntry(OrderEntry entry) {
        this.entry = entry;
    }
}
