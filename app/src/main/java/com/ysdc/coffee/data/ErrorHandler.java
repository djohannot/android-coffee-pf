package com.ysdc.coffee.data;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by david on 3/4/18.
 */

public class ErrorHandler {
    private final Context context;
    private PublishSubject<String> errorSubject = PublishSubject.create();

    public ErrorHandler(Context context){
        this.context = context;
    }

    public void addError(int errorStringReference){
        errorSubject.onNext(context.getString(errorStringReference));
    }

    public Observable<String> subscribeGeneralError(){
        return errorSubject;
    }
}
