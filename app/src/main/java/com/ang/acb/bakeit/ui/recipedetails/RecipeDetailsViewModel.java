package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 */
public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<RecipeDetails> recipeDetailsLiveData;
    private MutableLiveData<Integer> recipeIdLiveData = new MutableLiveData<>();

    @Inject
    public RecipeDetailsViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId) {
        recipeIdLiveData.setValue(recipeId);
    }

    public LiveData<RecipeDetails> getRecipeDetailsLiveData() {
        // Prevent NullPointerException
        if (recipeDetailsLiveData == null) {
            recipeDetailsLiveData = Transformations.switchMap(
                    recipeIdLiveData, repository::getRecipeDetails);
        }
        return recipeDetailsLiveData;
    }
}
