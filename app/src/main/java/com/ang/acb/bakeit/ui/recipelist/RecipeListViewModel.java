package com.ang.acb.bakeit.ui.recipelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Stores and manages UI-related data in a lifecycle conscious way.
 *
 * See: https://medium.com/androiddevelopers/viewmodels-and-livedata-patterns-antipatterns-21efaef74a54
 */
public class RecipeListViewModel extends ViewModel {

    private RecipeRepository repository;
    private LiveData<Resource<List<Recipe>>> recipesLiveData;
    private MutableLiveData<Boolean> retryLiveData;
    private MediatorLiveData<Resource<List<Recipe>>> result;

    @Inject
    public RecipeListViewModel(RecipeRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<Recipe>>> getRecipesLiveData() {
        if (recipesLiveData == null) {
            recipesLiveData = repository.getRecipeList();
        }
        return recipesLiveData;
    }

    public MutableLiveData<Boolean> getRetryLiveData() {
        if (retryLiveData == null) {
            retryLiveData = new MutableLiveData<>();
            retryLiveData.setValue(repository.getRetry());
        }
        return retryLiveData;
    }

    public void setRetryLiveData(boolean value) {
        if (retryLiveData == null) {
            retryLiveData = new MutableLiveData<>();
        }
        retryLiveData.setValue(value);
    }

    public MediatorLiveData<Resource<List<Recipe>>> getResult() {
        if (result == null) {
            setResult();
        }
        return result;
    }

    public void setResult() {
        if (result == null) {
            result = new MediatorLiveData<>();
        }
        LiveData<Resource<List<Recipe>>> recipesLiveData = getRecipesLiveData();
        MutableLiveData<Boolean> retryLiveData = getRetryLiveData();
        result.addSource(recipesLiveData, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> listResource) {
                result.setValue(listResource);
            }
        });
        result.addSource(retryLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                result.setValue(getRecipesLiveData().getValue());
            }
        });

    }

    // FIXME Retry any failed requests.
    // See: https://stackoverflow.com/questions/54087466/refreshing-livedata-with-retrofit-request-response
    // See: https://stackoverflow.com/questions/55913293/is-this-the-right-way-to-have-a-button-that-retries-an-api-call-on-an-android-ap
    public void retry() {
        Timber.d("Retry: update retry live data.");
        setRetryLiveData(true);
    }
}
