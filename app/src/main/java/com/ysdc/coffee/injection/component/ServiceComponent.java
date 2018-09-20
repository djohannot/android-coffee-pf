package com.ysdc.coffee.injection.component;

import com.ysdc.coffee.injection.annotations.ServiceScope;
import com.ysdc.coffee.injection.module.ServiceModule;
import com.ysdc.coffee.services.PushInstanceIdService;
import com.ysdc.coffee.services.PushMessagingService;

import dagger.Subcomponent;

/**
 * Created by david on 5/3/18.
 */
@ServiceScope
@Subcomponent(modules = {ServiceModule.class})
public interface ServiceComponent {

    void inject(PushMessagingService pushMessagingService);

    void inject(PushInstanceIdService pushInstanceIdService);
}
