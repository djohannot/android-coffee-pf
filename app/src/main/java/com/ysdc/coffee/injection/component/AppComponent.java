package com.ysdc.coffee.injection.component;

import com.ysdc.coffee.app.MyApplication;
import com.ysdc.coffee.injection.module.ActivityModule;
import com.ysdc.coffee.injection.module.AnalyticsModule;
import com.ysdc.coffee.injection.module.AppModule;
import com.ysdc.coffee.injection.module.FragmentModule;
import com.ysdc.coffee.injection.module.NetworkModule;
import com.ysdc.coffee.injection.module.RepositoryModule;
import com.ysdc.coffee.injection.module.ServiceModule;
import com.ysdc.coffee.injection.module.UtilsModule;
import com.ysdc.coffee.injection.module.ViewModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, NetworkModule.class, RepositoryModule.class, AnalyticsModule.class, UtilsModule.class})
@Singleton
public interface AppComponent {
    ActivityComponent childActivityComponent(ActivityModule activityModule);

    FragmentComponent childFragmentComponent(FragmentModule fragmentModule);

    ServiceComponent childServiceComponent(ServiceModule serviceModule);

    ViewComponent childViewComponent(ViewModule viewModule);

    void inject(MyApplication propertyFinderApplication);

}
