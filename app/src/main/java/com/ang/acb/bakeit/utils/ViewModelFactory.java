package com.ang.acb.bakeit.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ang.acb.bakeit.data.repo.RecipeRepository;
import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsViewModel;
import com.ang.acb.bakeit.ui.recipelist.RecipeListViewModel;

/**
 * A factory class for creating ViewModels with a constructor that takes a [MovieRepository].
 *
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/viewmodels
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    // Each view model needs access to the repository that handles the local and remote data.
    private final RecipeRepository repository;

    public static ViewModelFactory getInstance(RecipeRepository repository) {
        return new ViewModelFactory(repository);
    }

    private ViewModelFactory(RecipeRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeListViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeListViewModel(repository);
        } else if (modelClass.isAssignableFrom(RecipeDetailsViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeDetailsViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}