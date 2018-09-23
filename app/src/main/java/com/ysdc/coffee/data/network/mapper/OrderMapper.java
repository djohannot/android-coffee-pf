package com.ysdc.coffee.data.network.mapper;

import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.OrderProducts;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.model.User;
import com.ysdc.coffee.data.model.UserOrder;
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

    public OrderMapper() {
        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    public OrderRequest convertOrder(Order order) {
        OrderRequest orderRequest = new OrderRequest();
        for (OrderEntry orderEntry : order.getOrderedProductList()) {
            Item item = new Item();
            item.setId(orderEntry.getProduct().getId());
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

    public List<OrderedProduct> parseNetworkOrders(List<NetworkOrder> networkOrders) {
        List<OrderedProduct> orders = new ArrayList<>();

        for (NetworkOrder networkOrder : networkOrders) {
            Order order = new Order();
            order.setId(networkOrder.getId());
            order.setStatus(networkOrder.getOrderStatus());
            order.setUser(new User(networkOrder.getUserName(), networkOrder.getImageUrl()));
            try {
                order.setDate(simpleDateFormat.parse(networkOrder.getDate().split("\\.")[0]));
            } catch (ParseException e) {
                Timber.e(e,"oups");
            }
            for (Item item : networkOrder.getItems()) {
                orders.add(parseItem(item, order));
            }
        }
        return orders;
    }

    public List<UserOrder> parseAllUsersOrders(List<NetworkOrder> networkOrders) {
        List<UserOrder> orders = new ArrayList<>();

        for (NetworkOrder networkOrder : networkOrders) {
            UserOrder order = new UserOrder();
            order.setId(networkOrder.getId());
            order.setStatus(networkOrder.getOrderStatus());
            order.setUser(new User(networkOrder.getUserName(), networkOrder.getImageUrl()));
            try {
                order.setDate(simpleDateFormat.parse(networkOrder.getDate().split("\\.")[0]));
            } catch (ParseException e) {
                Timber.e(e,"oups");
            }
            for (Item item : networkOrder.getItems()) {
                order.getOrderedProductList().add(parseItem(item));
            }
            orders.add(order);
        }
        return orders;
    }

    private OrderProducts parseItem(Item item) {
        OrderProducts entry = new OrderProducts();
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
                entry.getIngredients().add(Ingredient.fromId(ingredient.getProductId()));
            }
        }
        return entry;
    }

    private OrderedProduct parseItem(Item item, Order order) {
        OrderedProduct orderedProduct = new OrderedProduct(order);
        orderedProduct.setCupSize(CupSize.fromId(item.getSize()));
        orderedProduct.setQuantity(item.getQuantity());
        orderedProduct.setSugarQuantity(item.getSugarQuantity());
        orderedProduct.setTakeaway(item.isTakeaway());
        orderedProduct.setCoffeeName(item.getCoffeeName());
        orderedProduct.setCoffeeImageUrl(item.getCoffeeImage());
        orderedProduct.setCoffeeId(item.getCoffeeId());
        orderedProduct.setCoffeeImageUrl(item.getCoffeeImage());
        orderedProduct.setNote(item.getNote());
        if (item.hasIngredients()) {
            for (OrderIngredient ingredient : item.getIngredients()) {
                orderedProduct.getIngredients().add(Ingredient.fromId(ingredient.getProductId()));
            }
        }
        return orderedProduct;
    }
}
