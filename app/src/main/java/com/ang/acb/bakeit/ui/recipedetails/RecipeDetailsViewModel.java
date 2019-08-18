package com.ang.acb.bakeit.ui.recipedetails;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.utils.SingleLiveEvent;

import java.util.List;
import java.util.Objects;


public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;

    private LiveData<WholeRecipe> wholeRecipeLiveData;
    private SingleLiveEvent<Integer> currentPositionLiveEvent = new SingleLiveEvent<>() ;

    private LiveData<List<Step>> stepsLiveData;
    private MutableLiveData<Step> currentStepLiveData = new MutableLiveData<>();

    boolean isTwoPane;


    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId, boolean isTwoPane) {
        wholeRecipeLiveData = repository.getFullRecipe(recipeId);
        stepsLiveData = repository.getRecipeSteps(recipeId);

        if (isTwoPane) setCurrentPositionLiveEvent(0);
    }


    public LiveData<WholeRecipe> getWholeRecipeLiveData() {
        return wholeRecipeLiveData;
    }

    public LiveData<Step> getCurrentStepLiveData(int position) {
        return currentStepLiveData;
    }

    public void setCurrentStepLiveData(int position) {
        if (stepsLiveData != null & stepsLiveData.getValue() != null) {
            currentStepLiveData.setValue(stepsLiveData.getValue().get(position));
        }
    }

    public void setCurrentStepLiveData (Step step) {
        currentStepLiveData.setValue(step);
    }

    public LiveData<List<Step>> getStepsLiveData() {
        return stepsLiveData;
    }

    public SingleLiveEvent<Integer> getCurrentPositionLiveEvent() {
        return currentPositionLiveEvent;
    }

    public void setCurrentPositionLiveEvent(int position){
        currentPositionLiveEvent.setValue(position);
    }

    public boolean hasPrevious(int currentPosition) {
        return currentPosition > 0;
    }

    public boolean hasNext(int currentPosition) {
        // FIXME: NullPointerException
        // return currentPosition < Objects.requireNonNull(stepsLiveData.getValue()).size() - 1;
        return false;
    }

    public void nextStep(int position) {
        if (hasNext(position))setCurrentStepLiveData(position+1);
    }

    public void previousStep(int position) {
        if (hasPrevious(position)) setCurrentStepLiveData(position-1);
    }
}
