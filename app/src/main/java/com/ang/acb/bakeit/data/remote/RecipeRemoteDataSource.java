package com.ang.acb.bakeit.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class RecipeRemoteDataSource {

    private final ApiService apiService;

    @Inject
    public RecipeRemoteDataSource(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ApiResponse<List<Recipe>>> loadAllRecipes () {
        Timber.d("Loading all the recipes from network.");
        return apiService.getAllRecipes();
    }
}

