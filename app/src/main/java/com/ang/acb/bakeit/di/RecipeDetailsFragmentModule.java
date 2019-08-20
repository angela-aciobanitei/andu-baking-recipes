package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RecipeDetailsFragmentModule {

    @ContributesAndroidInjector()
    abstract RecipeDetailsFragment contributeRecipeDetailsFragment();
}
