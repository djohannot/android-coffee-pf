package com.ysdc.coffee.injection.component;

import com.ysdc.coffee.injection.annotations.FragmentScope;
import com.ysdc.coffee.injection.module.FragmentModule;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

}
