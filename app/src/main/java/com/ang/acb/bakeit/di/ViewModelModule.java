package com.ang.acb.bakeit.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsViewModel;
import com.ang.acb.bakeit.ui.recipelist.RecipeListViewModel;
import com.ang.acb.bakeit.ui.viewmodel.RecipeViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel.class)
    abstract ViewModel bindRecipeListViewModel(RecipeListViewModel recipeListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailsViewModel.class)
    abstract ViewModel bindRecipeDetailsViewModel(RecipeDetailsViewModel recipeDetailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(RecipeViewModelFactory cakeViewModelFactory);
}
