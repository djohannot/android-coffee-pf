package com.ysdc.coffee.ui.create;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import java.util.List;

import io.reactivex.Completable;

public class CreateOrderPresenter<V extends CreateOrderMvpView> extends BasePresenter<V> implements CreateOrderMvpPresenter<V> {

    private OrderEntry entry;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public CreateOrderPresenter(ErrorHandler errorHandler, OrderRepository orderRepository, ProductRepository productRepository) {
        super(errorHandler);
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public OrderEntry getOrderEntry() {
        return entry;
    }

    @Override
    public void incrementQuantity() {
        entry.setQuantity(entry.getQuantity() + 1);
        getMvpView().updateQuantity(entry.getQuantity());
    }

    @Override
    public void decrementQuantity() {
        if (entry.getQuantity() > 0) {
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
    public void ingredientModified(String ingredientId, boolean selected) {
        if (selected) {
            entry.getIngredients().add(new Ingredient(ingredientId));
        } else {
            entry.getIngredients().remove(new Ingredient(ingredientId));
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

    @Override
    public List<Ingredient> getIngredients() {
        return productRepository.getIngredients();
    }

    @Override
    public boolean getSwitchValueForIngredient(Ingredient ingredient) {
        return entry.getIngredients().contains(ingredient);
    }
}
