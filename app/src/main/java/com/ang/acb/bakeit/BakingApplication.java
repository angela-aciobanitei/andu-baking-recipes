package com.ang.acb.bakeit;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.ang.acb.bakeit.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;


/**
 * When using Dagger for injecting Activity objects, you need to make your Application
 * implement HasAndroidInjector and @Inject a DispatchingAndroidInjector<Object> to
 * return from the androidInjector() method. See: https://dagger.dev/android.html.
 */
public class BakingApplication  extends Application implements HasActivityInjector, HasServiceInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }
}
