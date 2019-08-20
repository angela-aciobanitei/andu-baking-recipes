package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.utils.SingleLiveEvent;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;

    private LiveData<WholeRecipe> wholeRecipeLiveData;
    private SingleLiveEvent<Integer> openStepDetailsEvent = new SingleLiveEvent<>() ;

    private LiveData<List<Step>> stepsLiveData;
    private MediatorLiveData<Step> currentStepLiveData;
    private MutableLiveData<Integer> stepIndexLiveData;

    @Inject
    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId) {
        wholeRecipeLiveData = repository.getWholeRecipe(recipeId);
        stepsLiveData = repository.getRecipeSteps(recipeId);
    }

    public LiveData<WholeRecipe> getWholeRecipeLiveData() {
        return wholeRecipeLiveData;
    }

    public SingleLiveEvent<Integer> getOpenStepDetailsEvent() {
        return openStepDetailsEvent;
    }

    public void setOpenStepDetailsEvent(int position){
        openStepDetailsEvent.setValue(position);
    }

    public LiveData<List<Step>> getStepsLiveData() {
        return stepsLiveData;
    }

    public int getStepCount(){
        return Objects.requireNonNull(stepsLiveData.getValue()).size();
    }

    public LiveData<Integer> getStepIndex() {
        if (stepIndexLiveData == null) {
            setStepIndex(0);
        }
        return stepIndexLiveData;
    }

    public void setStepIndex(int index){
        if (stepIndexLiveData == null) {
            stepIndexLiveData = new MutableLiveData<>();
            stepIndexLiveData.setValue(0);
        }
        stepIndexLiveData.setValue(index);
    }


    public void nextStepIndex() {
        stepIndexLiveData.setValue(Objects.requireNonNull(getStepIndex().getValue()) + 1);
    }

    public void previousStepIndex() {
        stepIndexLiveData.setValue(Objects.requireNonNull(getStepIndex().getValue()) - 1);
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

        currentStepLiveData.addSource(stepsLiveData, steps -> {
            if (steps != null && stepIndexLiveData.getValue() != null) {
                currentStepLiveData.setValue(steps.get(stepIndexLiveData.getValue()));
            }
        });

        currentStepLiveData.addSource(stepIndexLiveData, stepIndex -> {
            if (stepIndex != null && stepsLiveData.getValue()!= null) {
                currentStepLiveData.setValue(stepsLiveData.getValue().get(stepIndex));
            }
        });
    }
}
