package com.ysdc.coffee.data.repository;

import com.google.firebase.auth.FirebaseUser;
import com.ysdc.coffee.data.prefs.MyPreferences;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;

import static com.ysdc.coffee.utils.AppConstants.EMPTY_STRING;

public class UserRepository {

    private static final int TIMEOUT_DURATION = 60;
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;

    private final MyPreferences preferences;

    private FirebaseUser firebaseUser;

    public UserRepository(MyPreferences preferences) {
        this.preferences = preferences;
    }

    public Completable registerUser(String idToken, String displayName, String email) {
        //TODO
        return Completable.complete();
    }

    public void logout() {
        preferences.set(MyPreferences.USER_TOKEN, EMPTY_STRING);
        preferences.set(MyPreferences.USER_MAIL, EMPTY_STRING);
        preferences.set(MyPreferences.USER_NAME, EMPTY_STRING);
        preferences.set(MyPreferences.USER_PICTURE, EMPTY_STRING);
    }

    public boolean isLoggedIn() {
        return !preferences.getAsString(MyPreferences.USER_TOKEN, EMPTY_STRING).isEmpty();
    }
}
