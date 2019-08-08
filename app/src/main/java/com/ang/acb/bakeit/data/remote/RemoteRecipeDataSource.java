package com.ang.acb.bakeit.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.utils.AppExecutors;

import java.util.List;

public class RemoteRecipeDataSource {

    // For Singleton instantiation.
    private static volatile RemoteRecipeDataSource sInstance;

    private final ApiService apiService;
    private final AppExecutors appExecutors;

    // Prevent direct instantiation.
    private RemoteRecipeDataSource(ApiService apiService, AppExecutors appExecutors) {
        this.apiService = apiService;
        this.appExecutors = appExecutors;
    }

    // Returns the single instance of this class, creating it if necessary.
    public static RemoteRecipeDataSource getInstance(ApiService apiService,
                                                     AppExecutors appExecutors){
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new RemoteRecipeDataSource(apiService, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ApiResponse<List<Recipe>>> loadAllRecipes () {
        return apiService.getAllRecipes();
    }
}

