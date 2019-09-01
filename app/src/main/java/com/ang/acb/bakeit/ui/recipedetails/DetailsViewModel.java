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


public class DetailsViewModel extends ViewModel {

    private RecipeRepository repository;

    private LiveData<RecipeDetails> recipeDetailsLiveData;
    private LiveData<List<Step>> stepsLiveData;
    private LiveData<List<Ingredient>> ingredientsLiveData;
    private MediatorLiveData<Step> currentStepLiveData;
    private MutableLiveData<Integer> stepIndexLiveData;
    private MutableLiveData<Integer> recipeIdLiveData = new MutableLiveData<>();

    @Inject
    public DetailsViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId) {
        if (recipeDetailsLiveData == null) {
            recipeDetailsLiveData = Transformations.switchMap(
                    recipeIdLiveData, repository::getRecipeDetails);
        }
        recipeIdLiveData.setValue(recipeId);
    }

    public LiveData<RecipeDetails> getRecipeDetailsLiveData() {
//        // Prevent NullPointerException
//        if (recipeDetailsLiveData == null) {
//            recipeDetailsLiveData = Transformations.switchMap(
//                    recipeIdLiveData, repository::getRecipeDetails);
//        }
//        recipeIdLiveData.setValue(recipeId);
        return recipeDetailsLiveData;
    }

    public LiveData<List<Step>> getStepsLiveData() {
        // Prevent NullPointerException
        if(stepsLiveData == null) {
            stepsLiveData = Transformations.switchMap(
                    recipeIdLiveData, repository::getRecipeSteps);
        }
        return stepsLiveData;
    }

    public LiveData<List<Ingredient>> getIngredientsLiveData() {
        if(ingredientsLiveData == null) {
            ingredientsLiveData = Transformations.switchMap(
                    recipeIdLiveData, repository::getRecipeIngredients);
        }
        return ingredientsLiveData;
    }

    public int getStepCount(){
        return Objects.requireNonNull(getStepsLiveData().getValue()).size();
    }

    public LiveData<Integer> getStepIndex() {
        if (stepIndexLiveData == null) {
            stepIndexLiveData = new MutableLiveData<>();
            stepIndexLiveData.setValue(0);
        }
        return stepIndexLiveData;
    }

    public void setStepIndexLiveData(int value) {
        if (stepIndexLiveData == null) {
            stepIndexLiveData = new MutableLiveData<>();
        }
        stepIndexLiveData.setValue(value);
    }

    public void nextStepIndex() {
        if (hasNext()) {
            stepIndexLiveData.setValue(Objects.requireNonNull(getStepIndex().getValue()) + 1);
        }
    }

    public void previousStepIndex() {
        if (hasPrevious()) {
            stepIndexLiveData.setValue(Objects.requireNonNull(getStepIndex().getValue()) - 1);
        }
    }

    public boolean hasNext() {
        return Objects.requireNonNull(getStepIndex().getValue()) + 1 < getStepCount();
    }

    public boolean hasPrevious() {
        return Objects.requireNonNull(getStepIndex().getValue()) > 0;
    }

    public LiveData<Step> getCurrentStep() {
        if (currentStepLiveData == null) {
            setCurrentStep();
        }
        return currentStepLiveData;
    }

    private void setCurrentStep() {
        if (currentStepLiveData == null) {
            currentStepLiveData = new MediatorLiveData<>();
        }

        LiveData<List<Step>> stepsLiveData = getStepsLiveData();
        currentStepLiveData.addSource(stepsLiveData, steps -> {
            if (steps != null && stepIndexLiveData.getValue() != null) {
               currentStepLiveData.setValue(steps.get(stepIndexLiveData.getValue()));
            }
        });

        LiveData<Integer> stepIndexLiveData = getStepIndex();
        currentStepLiveData.addSource(stepIndexLiveData, stepIndex -> {
            if (stepIndex != null && stepsLiveData.getValue()!= null) {
                currentStepLiveData.setValue(stepsLiveData.getValue().get(stepIndex));
            }
        });
    }
}
