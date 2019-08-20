package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipelist.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();
}
