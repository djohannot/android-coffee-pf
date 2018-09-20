package com.ysdc.coffee.data.repository;

import com.crashlytics.android.Crashlytics;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.mapper.ProductMapper;
import com.ysdc.coffee.exception.NotLoggedException;
import com.ysdc.coffee.exception.ValidationException;
import com.ysdc.coffee.utils.CrashlyticsUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class ProductRepository {
    private static final int HTTP_VALIDATION_FAILED = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_NOT_FOUND = 404;

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private Map<String, Product> productsMap;

    public ProductRepository(DefaultNetworkServiceCreator networkServiceCreator) {
        this.networkServiceCreator = networkServiceCreator;
        productsMap = new HashMap<>();
    }

    public Map<String, Product> getProductsMap(){
        return productsMap;
    }

    public Single<List<Product>> getProducts() {
        return networkServiceCreator.getCoffeeService().getCoffees()
                .subscribeOn(Schedulers.io())
                .map(productResponses -> {
                    ProductMapper mapper = new ProductMapper();
                    List<Product> products =  mapper.parseResponseList(productResponses);
                    fillMap(products);
                    return products;
                }).onErrorResumeNext(throwable -> {
                    if (throwable instanceof HttpException && ((HttpException) throwable).code() == HTTP_VALIDATION_FAILED) {
                        Crashlytics.log("VALIDATION EXCEPTION during getProduct: " + throwable.getMessage());
                        return Single.error(new ValidationException(throwable.getMessage()));
                    } else if (throwable instanceof HttpException && ((HttpException) throwable).code() == HTTP_UNAUTHORIZED) {
                        return Single.error(new NotLoggedException());
                    }
                    return Single.error(throwable);
                });
    }

    private void fillMap(List<Product> products) {
        for(Product product : products){
            productsMap.put(product.getId(), product);
        }
    }
}
