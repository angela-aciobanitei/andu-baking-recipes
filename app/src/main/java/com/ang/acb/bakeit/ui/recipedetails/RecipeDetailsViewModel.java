package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.utils.SingleLiveEvent;


public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<WholeRecipe> wholeRecipeLiveData;
    private MutableLiveData<Step> currentStep = new MutableLiveData<>();
    private final SingleLiveEvent<Integer> openStepDetailEvent = new SingleLiveEvent<>();

    public RecipeDetailsViewModel(final RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId) {
        wholeRecipeLiveData = repository.getFullRecipe(recipeId);
    }

    public LiveData<WholeRecipe> getWholeRecipeLiveData() {
        return wholeRecipeLiveData;
    }

    public LiveData<Step> getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int position) {
        //FIXME
        currentStep.setValue(wholeRecipeLiveData.getValue().getSteps().get(position));
        openStepDetailEvent.setValue(position);
    }

}
