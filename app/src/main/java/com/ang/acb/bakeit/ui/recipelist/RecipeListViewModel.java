package com.ang.acb.bakeit.ui.recipelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.repo.RecipeRepository;

import java.util.List;

import timber.log.Timber;

public class RecipeListViewModel extends ViewModel {

    private LiveData<Resource<List<Recipe>>> resourceLiveData;

    public RecipeListViewModel(RecipeRepository recipeRepository) {
        Timber.d("Use recipe repository to get recipe list");
        resourceLiveData = recipeRepository.getRecipes();
    }

    public LiveData<Resource<List<Recipe>>> getResourceLiveDataRecipes() {
        return resourceLiveData;
    }
}
