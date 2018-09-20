package com.ysdc.coffee.ui.base;


import com.ysdc.coffee.data.ErrorHandler;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by david on 26/2/18.
 */

public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    protected final CompositeDisposable compositeDisposable;
    private final ErrorHandler errorHandler;
    private V mvpView;


    // Only Presenters of Activities use this Constructor because the Remote Config Version Dialog is displayed at Activity level, would be redundant to add that capability at presenter Fragment level
    public BasePresenter(ErrorHandler errorHandler) {
        super();
        this.compositeDisposable = new CompositeDisposable();
        this.errorHandler = errorHandler;
    }

    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void onDetach() {
        compositeDisposable.dispose();
        mvpView = null;
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public V getMvpView() {
        return mvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) {
            //TODO
            //throw new MvpViewNotAttachedException();
        }
    }

    public CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

    private void subscribeNotifications() {
        compositeDisposable.add(errorHandler.subscribeGeneralError().subscribe(errorMessage -> getMvpView().onError(errorMessage)));
    }

}