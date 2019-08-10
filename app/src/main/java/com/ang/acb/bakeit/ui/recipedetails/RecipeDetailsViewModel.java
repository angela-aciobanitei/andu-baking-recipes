package com.ang.acb.bakeit.ui.recipedetails;

import androidx.annotation.VisibleForTesting;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.repo.RecipeRepository;

import java.util.List;

import timber.log.Timber;

public class RecipeDetailsViewModel extends ViewModel {

    @VisibleForTesting
    private MutableLiveData<Long> recipeIdLiveData;
    private LiveData<RecipeDetails> recipeDetailsLiveData;
    private RecipeRepository repository;

    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Long recipeId) {
        Timber.d("Initializing the recipe details view model");
        recipeIdLiveData = new MutableLiveData<>();
        // FIXME Get recipe details
        recipeDetailsLiveData = Transformations.switchMap(
                recipeIdLiveData,
                new Function<Long, LiveData<RecipeDetails>>() {
                    @Override
                    public LiveData<RecipeDetails> apply(Long input) {
                        return repository.getRecipeDetails(recipeId);
                    }
                }
        );

        recipeIdLiveData.setValue(recipeId);
    }



    public void retry() {
        Long currentId = recipeIdLiveData.getValue();
        if (currentId != null) {
            recipeIdLiveData.setValue(currentId);
        }
    }

    public LiveData<RecipeDetails> getRecipeDetailsLiveData() {
        return recipeDetailsLiveData;
    }

    public MutableLiveData<Long> getRecipeIdLiveData() {
        return recipeIdLiveData;
    }
}
