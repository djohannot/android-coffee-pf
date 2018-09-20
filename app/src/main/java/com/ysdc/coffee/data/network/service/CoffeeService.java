package com.ysdc.coffee.data.network.service;

import com.ysdc.coffee.data.network.model.Configuration;
import com.ysdc.coffee.data.network.model.NetworkOrder;
import com.ysdc.coffee.data.network.model.OrderRequest;
import com.ysdc.coffee.data.network.model.ProductResponse;
import com.ysdc.coffee.data.network.model.RegisterPush;
import com.ysdc.coffee.data.network.model.RegisterTokenResponse;
import com.ysdc.coffee.data.network.model.UpdateOrder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static com.ysdc.coffee.data.network.service.BaseService.HEADER_JSON;

public interface CoffeeService {
    String PARAMETER_ORDER_ID = "order_id";

    String ENDPOINT_COFFEE = "coffee";
    String ENDPOINT_ORDER = "order";
    String ENDPOINT_PUSH = "subscribe";
    String ENDPOINT_SETTINGS = "settings";
    String ENDPOINT_ORDER_STATUS = ENDPOINT_ORDER + "/{" + PARAMETER_ORDER_ID + "}";

    @Headers(HEADER_JSON)
    @GET(ENDPOINT_COFFEE)
    Single<List<ProductResponse>> getCoffees();

    @Headers(HEADER_JSON)
    @POST(ENDPOINT_ORDER)
    Single<NetworkOrder> placeOrder(@Body OrderRequest orderRequest);

    @Headers(HEADER_JSON)
    @PATCH(ENDPOINT_ORDER_STATUS)
    Single<NetworkOrder> updateOrder(@Path(PARAMETER_ORDER_ID) String orderId, @Body UpdateOrder updateOrder);

    @Headers(HEADER_JSON)
    @PATCH(ENDPOINT_ORDER_STATUS)
    Single<List<NetworkOrder>> getOrderStatus(@Path(PARAMETER_ORDER_ID) String orderId);

    @Headers(HEADER_JSON)
    @POST(ENDPOINT_PUSH)
    Single<RegisterTokenResponse> registerPushToken(@Body RegisterPush registerPush);

    @Headers(HEADER_JSON)
    @GET(ENDPOINT_ORDER)
    Single<List<NetworkOrder>> getOrders();

    @Headers(HEADER_JSON)
    @GET(ENDPOINT_SETTINGS)
    Single<Configuration> getSettings();
}
