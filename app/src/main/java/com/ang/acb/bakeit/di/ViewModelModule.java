package com.ang.acb.bakeit.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsViewModel;
import com.ang.acb.bakeit.ui.recipedetails.StepDetailsViewModel;
import com.ang.acb.bakeit.ui.recipelist.RecipeListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel.class)
    abstract ViewModel bindRecipeListViewModel(RecipeListViewModel recipeListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailsViewModel.class)
    abstract ViewModel bindRecipeDetailsViewModel(RecipeDetailsViewModel recipeDetailsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StepDetailsViewModel.class)
    abstract ViewModel bindStepDetailsViewModel (StepDetailsViewModel stepDetailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
