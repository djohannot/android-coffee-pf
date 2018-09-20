package com.ysdc.coffee.injection.component;

import com.ysdc.coffee.injection.annotations.FragmentScope;
import com.ysdc.coffee.injection.module.FragmentModule;
import com.ysdc.coffee.ui.bar.BarFragment;
import com.ysdc.coffee.ui.create.CreateOrderFragment;
import com.ysdc.coffee.ui.history.HistoryFragment;
import com.ysdc.coffee.ui.order.OrderFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(BarFragment barFragment);

    void inject(HistoryFragment historyFragment);

    void inject(OrderFragment orderFragment);

    void inject(CreateOrderFragment createOrderFragment);
}
