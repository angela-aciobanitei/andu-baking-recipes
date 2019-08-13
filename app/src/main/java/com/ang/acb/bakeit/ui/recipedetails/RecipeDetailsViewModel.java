package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.repo.RecipeRepository;
import com.ang.acb.bakeit.utils.DetailsLiveData;

public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<Recipe> simpleRecipeLiveData;
    private DetailsLiveData detailsLiveData;

    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Long recipeId) {
        simpleRecipeLiveData = repository.getSimpleRecipe(recipeId);
        detailsLiveData = repository.getDetailsPairLiveData(recipeId);
    }

    public LiveData<Recipe> getSimpleRecipeLiveData() {
        return simpleRecipeLiveData;
    }

    public DetailsLiveData getDetailsLiveData() {
        return detailsLiveData;
    }
}
