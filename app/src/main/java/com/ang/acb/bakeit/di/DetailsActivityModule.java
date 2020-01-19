package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DetailsActivityModule {

    @ContributesAndroidInjector(modules = DetailsBuildersModule.class)
    abstract DetailsActivity contributeDetailsActivity();
}
