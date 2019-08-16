package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.utils.SingleLiveEvent;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class StepDetailsViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<List<Step>> stepsLiveData;
    private int currentPosition;
    private final SingleLiveEvent<Step> navigateToStepDetails = new SingleLiveEvent<>();

    public StepDetailsViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId, int position) {
        Timber.d("Recipe [id=%s]: init step [position=%s] details view model",
                recipeId, position);

        stepsLiveData = repository.getRecipeSteps(recipeId);
        setCurrentPosition(position);
    }

    public LiveData<List<Step>> getStepsLiveData() {
        return stepsLiveData;
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
        navigateToCurrentStep();
    }

    public void nextStep() {
        currentPosition++;
        navigateToCurrentStep();
    }

    public void previousStep() {
        currentPosition--;
        navigateToCurrentStep();
    }

    public boolean hasNext() {
        return currentPosition < Objects.requireNonNull(stepsLiveData.getValue()).size() - 1;
    }

    public boolean hasPrevious() {
        return currentPosition > 0;
    }

    private void navigateToCurrentStep() {
        // FIXME
        MediatorLiveData<Step> stepMediatorLiveData = new MediatorLiveData<>();
        stepMediatorLiveData.addSource(stepsLiveData, new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                navigateToStepDetails.setValue(steps.get(currentPosition));
            }
        });

    }

    public SingleLiveEvent<Step> getNavigateToStepDetails() {
        return navigateToStepDetails;
    }

}
