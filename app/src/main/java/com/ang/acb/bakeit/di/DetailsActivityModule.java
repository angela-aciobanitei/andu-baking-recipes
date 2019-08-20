package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class DetailsActivityModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract DetailsActivity contributeDetailsActivity();
}
