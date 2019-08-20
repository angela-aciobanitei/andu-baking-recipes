package com.ang.acb.bakeit;

import android.app.Activity;
import android.app.Application;

import com.ang.acb.bakeit.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class BakingApplication  extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder().application(this).build().inject(this);

        // Timber is a logger with a small, extensible API which provides utility on top of
        // Android's normal Log class. Behavior is added through Tree instances. You can
        // install an instance by calling Timber.plant(). Installation of Trees should be
        // done as early as possible. The onCreate() of your app is the most logical choice.
        // See: https://www.androidhive.info/2018/11/android-implementing-logging-using-timber
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());



    }


}
