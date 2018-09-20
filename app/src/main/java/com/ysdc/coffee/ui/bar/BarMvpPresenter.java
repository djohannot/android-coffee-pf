package com.ysdc.coffee.ui.bar;

import com.ysdc.coffee.ui.base.MvpPresenter;

/**
 * Created by david on 26/2/18.
 */

public interface BarMvpPresenter<V extends BarMvpView> extends MvpPresenter<V> {

    void updateContent();
}