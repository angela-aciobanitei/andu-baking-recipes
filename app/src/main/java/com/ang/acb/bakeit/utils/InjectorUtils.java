package com.ang.acb.bakeit.utils;

import android.content.Context;

import com.ang.acb.bakeit.data.local.AppDatabase;
import com.ang.acb.bakeit.data.local.LocalRecipeDataSource;
import com.ang.acb.bakeit.data.remote.ApiClient;
import com.ang.acb.bakeit.data.remote.ApiService;
import com.ang.acb.bakeit.data.remote.RemoteRecipeDataSource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

/**
 * Enables injection of data sources.
 *
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/utilities
 */
public class InjectorUtils {

    private static LocalRecipeDataSource provideLocalRecipeDataSource(Context context){
        AppDatabase database = AppDatabase.getInstance(context);
        return LocalRecipeDataSource.getInstance(database);
    }

    private static RemoteRecipeDataSource provideRemoteRecipeDataSource(){
        ApiService apiService = ApiClient.getInstance();
        AppExecutors executors = AppExecutors.getInstance();
        return RemoteRecipeDataSource.getInstance(apiService, executors);
    }

    private static RecipeRepository provideRecipeRepository(Context context) {
        return RecipeRepository.getInstance(
                provideLocalRecipeDataSource(context),
                provideRemoteRecipeDataSource(),
                AppExecutors.getInstance());
    }
    public static ViewModelFactory provideViewModelFactory (Context context) {
        return ViewModelFactory.getInstance(provideRecipeRepository(context));
    }
}
