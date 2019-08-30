package com.ang.acb.bakeit.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.bakeit.ui.recipedetails.DetailsViewModel;
import com.ang.acb.bakeit.ui.recipelist.RecipeListViewModel;
import com.ang.acb.bakeit.ui.viewmodel.RecipeViewModelFactory;

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
    @ViewModelKey(DetailsViewModel.class)
    abstract ViewModel bindRecipeDetailsViewModel(DetailsViewModel detailsViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(RecipeViewModelFactory factory);
}
