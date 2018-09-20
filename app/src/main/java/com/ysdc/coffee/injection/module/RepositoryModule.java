package com.ysdc.coffee.injection.module;

import com.ysdc.coffee.data.prefs.MyPreferences;
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
    UserRepository provideSwissFloorballRepository(MyPreferences preferences) {
        return new UserRepository(preferences);
    }

}
