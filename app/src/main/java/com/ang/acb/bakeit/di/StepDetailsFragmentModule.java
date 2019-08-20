package com.ang.acb.bakeit.di;

import com.ang.acb.bakeit.ui.recipedetails.StepDetailsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class StepDetailsFragmentModule {

    @ContributesAndroidInjector()
    abstract StepDetailsFragment contributeStepDetailsFragment();
}
