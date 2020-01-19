package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipelist.RecipeListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainBuildersModule {

    @ContributesAndroidInjector()
    abstract RecipeListFragment contributeRecipeListFragment();
}
