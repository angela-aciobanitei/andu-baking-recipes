package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.utils.SingleLiveEvent;

import java.util.List;


public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<WholeRecipe> wholeRecipeLiveData;
    private LiveData<List<Step>> stepsLiveData;
    private MutableLiveData<Step> currentStep = new MutableLiveData<>();
    private final SingleLiveEvent<Integer> openStepDetailEvent = new SingleLiveEvent<>();

    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId, boolean isTwoPane) {
        wholeRecipeLiveData = repository.getFullRecipe(recipeId);
        stepsLiveData = repository.getRecipeSteps(recipeId);

        if(isTwoPane) setCurrentStep(0);
    }

    public LiveData<WholeRecipe> getWholeRecipeLiveData() {
        return wholeRecipeLiveData;
    }

    public LiveData<Step> getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int position) {
        //FIXME
        MediatorLiveData<Step> stepMediatorLiveData = new MediatorLiveData<>();
        stepMediatorLiveData.addSource(stepsLiveData, new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                currentStep.setValue(steps.get(position));

            }
        });
        openStepDetailEvent.setValue(position);
    }

    public SingleLiveEvent<Integer> getOpenStepDetailEvent() {
        return openStepDetailEvent;
    }

}
