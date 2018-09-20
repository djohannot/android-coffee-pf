package com.ysdc.coffee.data.network.mapper;

import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.network.model.ProductResponse;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public ProductMapper() {
        //Nothing to do
    }

    public List<Product> parseResponseList(List<ProductResponse> responseList) {
        List<Product> result = new ArrayList<>();
        for (ProductResponse response : responseList) {
            result.add(parseResponse(response));
        }
        return result;
    }

    private Product parseResponse(ProductResponse response) {
        return Product.builder()
                .withId(response.getId())
                .withName(response.getName())
                .withImageUrl(response.getImageUrl())
                .withDescription(response.getDescription())
                .build();
    }
}
