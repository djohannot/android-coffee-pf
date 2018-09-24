package com.ysdc.coffee.data.repository;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.mapper.ProductMapper;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.exception.NotLoggedException;
import com.ysdc.coffee.exception.ValidationException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

import static com.ysdc.coffee.data.prefs.MyPreferences.INGREDIENTS;

public class ProductRepository {
    private static final int HTTP_VALIDATION_FAILED = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_NOT_FOUND = 404;

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private final MyPreferences preferences;
    private final Gson gson;

    private final Map<String, Product> productsMap;
    private final Map<String, Ingredient> ingredients;

    public ProductRepository(MyPreferences preferences, DefaultNetworkServiceCreator networkServiceCreator, Gson gson) {
        this.networkServiceCreator = networkServiceCreator;
        this.preferences = preferences;
        this.gson = gson;

        this.productsMap = new HashMap<>();
        this.ingredients = new HashMap<>();
        loadFromStorage();
    }

    public Map<String, Product> getProductsMap() {
        return productsMap;
    }

    public Single<List<Product>> getProducts() {
        return networkServiceCreator.getCoffeeService().getCoffees()
                .subscribeOn(Schedulers.io())
                .map(productResponses -> {
                    ProductMapper mapper = new ProductMapper();
                    List<Product> products = mapper.parseResponseList(productResponses);
                    productsMap.clear();
                    for (Product product : products) {
                        productsMap.put(product.getId(), product);
                    }
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

    public Ingredient getIngredientForId(String id){
        return ingredients.get(id);
    }

    public List<Ingredient> getIngredients(){
        ArrayList<Ingredient> result = new ArrayList<>(ingredients.values());
        Collections.sort(result, (ingredient, t1) -> ingredient.getName().compareTo(t1.getName()));
        return result;
    }

    public Completable retrieveIngredients() {
        return networkServiceCreator.getCoffeeService().getIngredients()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(newIngredients -> {
                    ingredients.clear();
                    for (Ingredient ingredient : newIngredients) {
                        ingredients.put(ingredient.getId(), ingredient);
                    }
                    preferences.set(INGREDIENTS, ingredients);
                })
                .ignoreElement();
    }

    private void loadFromStorage() {
        Type ingredientsType = new TypeToken<Map<String, Ingredient>>() {
        }.getType();

        String ingredientsJsonFile = preferences.getAsString(INGREDIENTS);
        if (!ingredientsJsonFile.isEmpty()) {
            ingredients.putAll(gson.fromJson(ingredientsJsonFile, ingredientsType));
        }
    }
}
