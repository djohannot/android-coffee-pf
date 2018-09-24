package com.ysdc.coffee.data.network.mapper;

import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.User;
import com.ysdc.coffee.data.network.model.Item;
import com.ysdc.coffee.data.network.model.NetworkOrder;
import com.ysdc.coffee.data.network.model.OrderIngredient;
import com.ysdc.coffee.data.network.model.OrderRequest;
import com.ysdc.coffee.data.repository.ProductRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class OrderMapper {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final SimpleDateFormat simpleDateFormat;
    private final ProductRepository productRepository;

    public OrderMapper(ProductRepository productRepository) {
        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.productRepository = productRepository;
    }

    public OrderRequest convertOrder(Order order) {
        OrderRequest orderRequest = new OrderRequest();
        for (OrderEntry orderEntry : order.getEntries()) {
            Item item = new Item();
            item.setId(orderEntry.getCoffeeId());
            item.setQuantity(orderEntry.getQuantity());
            item.setNote(orderEntry.getNote());
            item.setTakeaway(orderEntry.isTakeaway());
            item.setSize(orderEntry.getCupSize().getValue());
            item.setSugarQuantity(orderEntry.getSugarQuantity());

            for (Ingredient ingredient : orderEntry.getIngredients()) {
                item.getIngredients().add(new OrderIngredient(ingredient.getId(), 1));
            }
            orderRequest.getItems().add(item);
        }
        return orderRequest;
    }

    public List<Order> convertNetworkOrder(List<NetworkOrder> networkOrders) {
        List<Order> orders = new ArrayList<>();

        for (NetworkOrder networkOrder : networkOrders) {
            Order order = new Order();
            order.setId(networkOrder.getId());
            order.setStatus(networkOrder.getOrderStatus());
            order.setUser(new User(networkOrder.getUserName(), networkOrder.getImageUrl()));
            try {
                order.setDate(simpleDateFormat.parse(networkOrder.getDate().split("\\.")[0]));
            } catch (ParseException e) {
                Timber.e(e, "Date parsing exception");
            }
            for (Item item : networkOrder.getItems()) {
                order.getEntries().add(parseItem(item));
            }
            orders.add(order);
        }
        return orders;
    }

    private OrderEntry parseItem(Item item) {
        OrderEntry entry = new OrderEntry();
        entry.setCupSize(CupSize.fromId(item.getSize()));
        entry.setQuantity(item.getQuantity());
        entry.setSugarQuantity(item.getSugarQuantity());
        entry.setTakeaway(item.isTakeaway());
        entry.setCoffeeName(item.getCoffeeName());
        entry.setCoffeeImageUrl(item.getCoffeeImage());
        entry.setCoffeeId(item.getCoffeeId());
        entry.setCoffeeImageUrl(item.getCoffeeImage());
        entry.setNote(item.getNote());
        if (item.hasIngredients()) {
            for (OrderIngredient ingredient : item.getIngredients()) {
                entry.getIngredients().add(productRepository.getIngredientForId(ingredient.getId()));
            }
        }
        return entry;
    }
}
