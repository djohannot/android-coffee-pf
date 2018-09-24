package com.ysdc.coffee.injection.module;

import com.google.gson.Gson;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.data.repository.PushNotificationRepository;
import com.ysdc.coffee.data.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by david on 2/8/18.
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    OrderRepository provideOrderRepository(DefaultNetworkServiceCreator networkServiceCreator, ProductRepository productRepository) {
        return new OrderRepository(networkServiceCreator, productRepository);
    }

    @Provides
    @Singleton
    ProductRepository provideProductRepository(DefaultNetworkServiceCreator networkServiceCreator, MyPreferences preferences, Gson gson) {
        return new ProductRepository(preferences, networkServiceCreator, gson);
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(MyPreferences preferences) {
        return new UserRepository(preferences);
    }

    @Provides
    @Singleton
    ConfigurationRepository provideConfigurationRepository(MyPreferences preferences, DefaultNetworkServiceCreator networkServiceCreator, Gson gson) {
        return new ConfigurationRepository(preferences, networkServiceCreator, gson);
    }

    @Provides
    @Singleton
    PushNotificationRepository providePushNotificationRepositoryRepository(MyPreferences preferences, DefaultNetworkServiceCreator networkServiceCreator) {
        return new PushNotificationRepository(preferences, networkServiceCreator);
    }

}
