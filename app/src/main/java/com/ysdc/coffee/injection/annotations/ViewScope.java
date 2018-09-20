package com.ysdc.coffee.injection.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by david on 26/2/18.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewScope {
}
