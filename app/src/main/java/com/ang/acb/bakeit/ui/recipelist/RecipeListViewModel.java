package com.ang.acb.bakeit.ui.recipelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeListViewModel extends ViewModel {

    private LiveData<Resource<List<Recipe>>> recipeListResourceLiveData;

    // FIXME: InstantiationException: RecipeListViewModel has no zero argument constructor
    //  See: https://stackoverflow.com/questions/53978885/java-lang-class-viewmodel-has-no-zero-argument-constructor

    @Inject
    public RecipeListViewModel(RecipeRepository repository) {
        recipeListResourceLiveData = repository.loadAllRecipes();
    }

    public LiveData<Resource<List<Recipe>>> getRecipeListResourceLiveData() {
        return recipeListResourceLiveData;
    }

    // FIXME Retry any failed requests.
    public void retry() {
    }
}
