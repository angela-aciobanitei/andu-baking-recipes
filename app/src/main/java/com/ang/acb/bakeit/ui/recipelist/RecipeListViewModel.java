package com.ang.acb.bakeit.ui.recipelist;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeListViewModel extends ViewModel {

    @VisibleForTesting
    private LiveData<Resource<List<Recipe>>> recipesLiveData;
    private RecipeRepository repository;

    @Inject
    public RecipeListViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<Recipe>>> getRecipesLiveData() {
        if (recipesLiveData == null) {
            recipesLiveData = repository.loadAllRecipes();
        }
        return recipesLiveData;
    }

    // FIXME Retry any failed requests.
    public void retry() {
        recipesLiveData = repository.loadAllRecipes();
    }
}
