package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.repository.RecipeRepository;


public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<WholeRecipe> wholeRecipeLiveData;

    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId) {
        wholeRecipeLiveData = repository.getFullRecipe(recipeId);
    }

    public LiveData<WholeRecipe> getWholeRecipeLiveData() {
        return wholeRecipeLiveData;
    }
}
