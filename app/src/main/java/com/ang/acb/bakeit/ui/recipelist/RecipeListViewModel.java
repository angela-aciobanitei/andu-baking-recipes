package com.ang.acb.bakeit.ui.recipelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.DetailedRecipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.repo.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private LiveData<Resource<List<DetailedRecipe>>> recipeListResourceLiveData;

    public RecipeListViewModel(RecipeRepository repository) {
        recipeListResourceLiveData = repository.loadAllRecipes();
    }

    public LiveData<Resource<List<DetailedRecipe>>> getRecipeListResourceLiveData() {
        return recipeListResourceLiveData;
    }

    // FIXME Retry any failed requests.
    public void retry() {
    }
}
