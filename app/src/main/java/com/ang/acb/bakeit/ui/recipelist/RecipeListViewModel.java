package com.ang.acb.bakeit.ui.recipelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Stores and manages UI-related data in a lifecycle conscious way.
 * https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 */
public class RecipeListViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<Resource<List<RecipeDetails>>> liveRecipes;

    @Inject
    public RecipeListViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<RecipeDetails>>> getLiveRecipes() {
        if (liveRecipes == null) {
            liveRecipes = repository.loadAllRecipes();
        }
        return liveRecipes;
    }

    public void retry() {
        Timber.d("Retry.");
    }
}
