package com.ysdc.coffee.injection.component;

import com.ysdc.coffee.injection.annotations.ViewScope;
import com.ysdc.coffee.injection.module.ViewModule;

import dagger.Subcomponent;


@ViewScope
@Subcomponent(modules = ViewModule.class)
public interface ViewComponent {

}
