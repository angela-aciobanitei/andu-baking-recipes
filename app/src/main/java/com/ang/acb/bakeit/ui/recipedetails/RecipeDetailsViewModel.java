package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.repo.RecipeRepository;

import timber.log.Timber;

public class RecipeDetailsViewModel extends ViewModel {

    private final RecipeRepository repository;
    private LiveData<Resource<RecipeDetails>> recipeDetailsLiveData;
    private MutableLiveData<Long> recipeIdLiveData = new MutableLiveData<>();

    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Long recipeId) {
        // Load recipe details only when the activity is created for the first time.
        if (recipeDetailsLiveData != null) return;

        Timber.d("Initializing recipe details view model");
        // FIXME: repository.getRecipes() needs to return a list of LiveData<Resource<RecipeDetails>>.
        // recipeDetailsLiveData = Transformations.switchMap(recipeIdLiveData, repository::getRecipes);

        recipeIdLiveData.setValue(recipeId);
    }

    public void retry(Long recipeId) {
        recipeIdLiveData.setValue(recipeId);
    }

    public LiveData<Resource<RecipeDetails>> getRecipeDetailsLiveData() {
        return recipeDetailsLiveData;
    }

    public MutableLiveData<Long> getRecipeIdLiveData() {
        return recipeIdLiveData;
    }
}
