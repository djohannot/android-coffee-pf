package com.ysdc.coffee.ui.status;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by david on 1/25/18.
 */

public class StatusPresenter<V extends StatusMvpView> extends BasePresenter<V> implements StatusMvpPresenter<V> {

    private final ConfigurationRepository configurationRepository;
    private final MyPreferences preferences;

    public StatusPresenter(ErrorHandler errorHandler, ConfigurationRepository configurationRepository, MyPreferences preferences) {
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

    @Override
    public Single<Boolean> switchStatus() {
        return configurationRepository.updateConfiguration(!configurationRepository.isBarOpen());
    }

}
