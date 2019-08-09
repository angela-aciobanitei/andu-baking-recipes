package com.ang.acb.bakeit.data.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.ang.acb.bakeit.data.local.LocalRecipeDataSource;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.remote.ApiResponse;
import com.ang.acb.bakeit.data.remote.RemoteRecipeDataSource;
import com.ang.acb.bakeit.utils.AppExecutors;

import java.util.List;

import timber.log.Timber;

/**
 * Repository module for handling data operations.
 *
 * See: https://developer.android.com/jetpack/docs/guide#truth
 * See: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
 */
public class RecipeRepository {

    // For Singleton instantiation.
    private static volatile RecipeRepository sInstance;

    private final LocalRecipeDataSource localDataSource;
    private final RemoteRecipeDataSource remoteDataSource;
    private final AppExecutors appExecutors;

    // Prevents direct instantiation.
    private RecipeRepository(LocalRecipeDataSource localDataSource,
                             RemoteRecipeDataSource remoteDataSource,
                             AppExecutors appExecutors) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.appExecutors = appExecutors;
    }

    // Returns the single instance of this class, creating it if necessary.
    public static RecipeRepository getInstance(LocalRecipeDataSource localDataSource,
                                               RemoteRecipeDataSource remoteDataSource,
                                               AppExecutors appExecutors){
        if(sInstance == null){
            synchronized (RecipeRepository.class) {
                if(sInstance == null) {
                    sInstance = new RecipeRepository(localDataSource, remoteDataSource, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<Resource<List<Recipe>>> getRecipes(){
        // Note that we are using the NetworkBoundResource<ResultType, RequestType> class
        // that we've created earlier which can provide a resource backed by both the
        // SQLite database and the network. It defines two type parameters, ResultType
        // and RequestType, because the data type used locally might not match the data
        // type returned from the API.
        // TODO ResultType ==> the data type used locally.
        // TODO RequestType => the data type returned from the API.
        return new NetworkBoundResource<List<Recipe>, List<Recipe>>(appExecutors) {


            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Recipe>>> createCall() {
                // Create the API call to load the all recipes.
                Timber.d("Loading all the recipes from network.");
                return remoteDataSource.loadAllRecipes();
            }

            @Override
            protected void saveCallResult(@NonNull List<Recipe> recipeList) {
                // Save the result of the API response into the database.
                localDataSource.saveAllRecipes(recipeList);
                Timber.d("%s recipes saved to database.", recipeList.size());
            }

            @Override
            protected void onFetchFailed() {
                // Ignored
                Timber.d("Fetch failed!");
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                // Only fetch fresh data if it doesn't exist in database.
                return data == null || data.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                // Get the cached data from the database.
                Timber.d("Getting all the recipes from database.");
                return localDataSource.getAllRecipes();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<RecipeDetails>> getRecipeDetails(Long recipeId){
        return  localDataSource.getRecipeDetails(recipeId);
    }
}
