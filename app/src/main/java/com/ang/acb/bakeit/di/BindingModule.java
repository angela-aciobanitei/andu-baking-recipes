package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;
import com.ang.acb.bakeit.ui.recipelist.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BindingModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract DetailsActivity contributeDetailsActivity();
}
