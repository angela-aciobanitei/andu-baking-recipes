package com.ang.acb.bakeit;

import android.app.Application;

import timber.log.Timber;

/**
 * Timber is a logger with a small, extensible API which provides utility on top of
 * Android's normal Log class. Behavior is added through Tree instances. You can install
 * an instance by calling Timber.plant(). Installation of Trees should be done as early as
 * possible. The onCreate() of your application is the most logical choice.
 *
 * See: https://github.com/JakeWharton/timber
 * See: https://www.androidhive.info/2018/11/android-implementing-logging-using-timber/
 */
public class BakingApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
