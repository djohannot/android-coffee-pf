package com.ysdc.coffee.ui.status;

import com.ysdc.coffee.ui.base.MvpPresenter;

import io.reactivex.Single;

/**
 * Created by david on 26/2/18.
 */

public interface StatusMvpPresenter<V extends StatusMvpView> extends MvpPresenter<V> {

    void updateContent();

    Single<Boolean> switchStatus();
}