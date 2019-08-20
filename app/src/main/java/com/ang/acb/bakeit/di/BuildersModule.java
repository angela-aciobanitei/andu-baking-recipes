package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;
import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsFragment;
import com.ang.acb.bakeit.ui.recipedetails.StepDetailsFragment;
import com.ang.acb.bakeit.ui.recipelist.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = DetailsActivityModule.class)
    abstract DetailsActivity bindDetailsActivity();

    @ContributesAndroidInjector(modules = RecipeDetailsFragmentModule.class)
    abstract RecipeDetailsFragment bindRecipeDetailsFragment();

    @ContributesAndroidInjector(modules = StepDetailsFragmentModule.class)
    abstract StepDetailsFragment bindStepDetailsFragment();
}
