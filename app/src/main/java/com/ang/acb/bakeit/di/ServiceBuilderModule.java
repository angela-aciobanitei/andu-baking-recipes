package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.widget.RecipeRemoteViewsService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ServiceBuilderModule {
    @ContributesAndroidInjector
    abstract RecipeRemoteViewsService contributeRecipeRemoteViewsService();
}
