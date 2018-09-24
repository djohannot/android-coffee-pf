package com.ysdc.coffee.ui.bar;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by david on 1/25/18.
 */

public class BarPresenter<V extends BarMvpView> extends BasePresenter<V> implements BarMvpPresenter<V> {

    private final ConfigurationRepository configurationRepository;

    public BarPresenter(ErrorHandler errorHandler, ConfigurationRepository configurationRepository) {
        super(errorHandler);
        this.configurationRepository = configurationRepository;
    }

    @Override
    public void updateContent() {
        getCompositeDisposable().add(configurationRepository.refreshConfiguration()
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .doOnComplete(() -> getMvpView().updateBar(configurationRepository.isBarOpen()))
                .subscribe());
    }

}
