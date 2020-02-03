package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Stores and manages UI-related data in a lifecycle conscious way.
 * https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 */
public class StepDetailsViewModel extends ViewModel {

    private RecipeRepository repository;

    private MutableLiveData<Integer> liveRecipeId = new MutableLiveData<>();
    private MutableLiveData<Integer> liveStepPosition = new MutableLiveData<>();
    private LiveData<List<Step>> liveSteps;
    private int stepsSize;


    @Inject
    public StepDetailsViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public void init(Integer recipeId, Integer stepIndex) {
        liveRecipeId.setValue(recipeId);
        liveStepPosition.setValue(stepIndex);
    }


    public LiveData<List<Step>> getLiveSteps() {
        if(liveSteps == null) {
            liveSteps = Transformations.switchMap(liveRecipeId, repository::getRecipeSteps);
        }
        return liveSteps;
    }

    public MutableLiveData<Integer> getLiveStepPosition() {
        return liveStepPosition;
    }

    // See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
    public MediatorLiveData<Resource<Step>> getCurrentStep() {

        LiveData<Integer> positionResult = getLiveStepPosition();
        LiveData<List<Step>> stepsResult = getLiveSteps();
        MediatorLiveData<Resource<Step>> result = new MediatorLiveData<>();

        result.addSource(positionResult, newData ->
                result.setValue(combineLatestData(positionResult, stepsResult)));

        result.addSource(stepsResult, newData ->
                result.setValue(combineLatestData(positionResult, stepsResult)));

        return result;
    }

    private Resource<Step> combineLatestData(LiveData<Integer> positionResult,
                                             LiveData<List<Step>> stepsResult ) {
        Integer position = positionResult.getValue();
        List<Step> steps = stepsResult.getValue();

        // Don't send a success until we have both results.
        if(position == null || steps == null) {
            return Resource.loading(null);
        } else if (position != null && steps != null) {
            stepsSize = steps.size();
            return Resource.success(steps.get(position));
        } else {
            return Resource.error("Error", null);
        }
    }

    public int getStepsSize(){
        return stepsSize;
    }

    public boolean hasPrevious() {
        return Objects.requireNonNull(liveStepPosition.getValue()) > 0;
    }

    public boolean hasNext() {
        return Objects.requireNonNull(liveStepPosition.getValue()) + 1 < getStepsSize();
    }

    public void onPrevious() {
        if (hasPrevious()) {
            liveStepPosition.setValue(Objects.requireNonNull(liveStepPosition.getValue()) - 1);
        }
    }

    public void onNext() {
        if (hasNext()) {
            liveStepPosition.setValue(Objects.requireNonNull(liveStepPosition.getValue()) + 1);
        }
    }

    public void retry(Integer recipeId) {
        liveRecipeId.setValue(recipeId);
    }
}
