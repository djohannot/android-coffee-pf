package com.ysdc.coffee.ui.create;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import io.reactivex.Completable;

public class CreateOrderPresenter<V extends CreateOrderMvpView> extends BasePresenter<V> implements CreateOrderMvpPresenter<V> {

    private OrderedProduct orderedProduct;
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
        return orderedProduct.getProduct();
    }

    @Override
    public void incrementQuantity() {
        orderedProduct.setQuantity(orderedProduct.getQuantity()+1);
        getMvpView().updateQuantity(orderedProduct.getQuantity());
    }

    @Override
    public void decrementQuantity() {
        if(orderedProduct.getQuantity()>0) {
            orderedProduct.setQuantity(orderedProduct.getQuantity() - 1);
            getMvpView().updateQuantity(orderedProduct.getQuantity());
        }
    }

    @Override
    public void setSizeSelected(CupSize cupSize) {
        orderedProduct.setCupSize(cupSize);
    }

    @Override
    public void setSugarSelected(int quantity) {
        orderedProduct.setSugarQuantity(quantity);
    }

    @Override
    public void carmelSelected(boolean selected) {
        if (selected){
            orderedProduct.getIngredients().add(Ingredient.CARMEL);
        }else{
            orderedProduct.getIngredients().remove(Ingredient.CARMEL);
        }
    }

    @Override
    public void toffeenutSelected(boolean selected) {
        if (selected){
            orderedProduct.getIngredients().add(Ingredient.TOFFEENUT);
        }else{
            orderedProduct.getIngredients().remove(Ingredient.TOFFEENUT);
        }
    }

    @Override
    public void vanillaSelected(boolean selected) {
        if (selected){
            orderedProduct.getIngredients().add(Ingredient.VANILLA);
        }else{
            orderedProduct.getIngredients().remove(Ingredient.VANILLA);
        }
    }

    @Override
    public void isTakeAway(boolean isTakeAway) {
        orderedProduct.setTakeaway(isTakeAway);
    }

    @Override
    public Completable addOrder() {
        return Completable.defer(() -> {
            orderRepository.addProductToOrder(orderedProduct);
            return Completable.complete();
        });
    }

    @Override
    public void setOrderProduct(OrderedProduct orderProduct) {
        this.orderedProduct = orderProduct;
    }
}
