package com.ysdc.coffee.data.network.mapper;

import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
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

public class OrderMapper {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final SimpleDateFormat simpleDateFormat;
    private final ProductRepository productRepository;

    public OrderMapper(ProductRepository productRepository) {
        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        this.productRepository = productRepository;
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
                order.setDate(simpleDateFormat.parse(networkOrder.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (Item item : networkOrder.getItems()) {
                orders.add(parseItem(item, order));
            }
        }
        return orders;
    }


    private OrderedProduct parseItem(Item item, Order order) {
        Product product = productRepository.getProductsMap().get(item.getId());
        OrderedProduct orderedProduct = new OrderedProduct(product, order);
        orderedProduct.setCupSize(CupSize.fromId(item.getSize()));
        orderedProduct.setQuantity(item.getQuantity());
        orderedProduct.setSugarQuantity(item.getSugarQuantity());
        orderedProduct.setTakeaway(item.isTakeaway());
        orderedProduct.setNote(item.getNote());
        for (OrderIngredient ingredient : item.getIngredients()) {
            orderedProduct.getIngredients().add(Ingredient.fromId(ingredient.getProductId()));
        }
        return orderedProduct;
    }
}
