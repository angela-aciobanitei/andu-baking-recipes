package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsFragment;
import com.ang.acb.bakeit.ui.recipedetails.StepDetailsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 *
 * See:
 */
@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract RecipeDetailsFragment contributeRecipeDetailsFragment();

    @ContributesAndroidInjector()
    abstract StepDetailsFragment contributeStepDetailsFragment();
}