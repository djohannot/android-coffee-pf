package com.ysdc.coffee.injection.module;

import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
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
    OrderRepository provideOrderRepository(DefaultNetworkServiceCreator networkServiceCreator) {
        return new OrderRepository(networkServiceCreator);
    }

    @Provides
    @Singleton
    ProductRepository provideProductRepository(DefaultNetworkServiceCreator networkServiceCreator) {
        return new ProductRepository(networkServiceCreator);
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(MyPreferences preferences, DefaultNetworkServiceCreator networkServiceCreator) {
        return new UserRepository(preferences, networkServiceCreator);
    }

}
