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
    private final MyPreferences preferences;

    public BarPresenter(ErrorHandler errorHandler, ConfigurationRepository configurationRepository, MyPreferences preferences) {
        super(errorHandler);
        this.configurationRepository = configurationRepository;
        this.preferences = preferences;
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
