package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.repo.RecipeRepository;


public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;
    private MutableLiveData<Long> recipeIdLiveData = new MutableLiveData<>();
    private LiveData<RecipeDetails> recipeDetailsLiveData;

    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Long recipeId) {
        recipeDetailsLiveData = Transformations.switchMap(
                recipeIdLiveData,
                input -> repository.getRecipeDetails(recipeId));

        recipeIdLiveData.setValue(recipeId);
    }

    public MutableLiveData<Long> getRecipeIdLiveData() {
        return recipeIdLiveData;
    }

    public LiveData<RecipeDetails> getRecipeDetailsLiveData() {
        return recipeDetailsLiveData;
    }
}
