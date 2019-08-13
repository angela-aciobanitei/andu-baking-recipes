package com.ang.acb.bakeit.data.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.ang.acb.bakeit.data.local.LocalRecipeDataSource;
import com.ang.acb.bakeit.data.model.DetailedRecipe;
import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.remote.ApiResponse;
import com.ang.acb.bakeit.data.remote.RemoteRecipeDataSource;
import com.ang.acb.bakeit.utils.AppExecutors;
import com.ang.acb.bakeit.utils.DetailsLiveData;

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

    public LiveData<Resource<List<DetailedRecipe>>> loadAllRecipes(){
        // Note that we are using the NetworkBoundResource<ResultType, RequestType> class
        // that we've created earlier which can provide a resource backed by both the
        // SQLite database and the network. It defines two type parameters, ResultType
        // and RequestType, because the data type used locally might not match the data
        // type returned from the API.
        return new NetworkBoundResource<List<DetailedRecipe>, List<Recipe>>(appExecutors) {

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Recipe>>> createCall() {
                // Create the API call to load the all recipes.
                // FIXME: The return type is the data type returned from the API.
                Timber.d("NetworkBoundResource createCall(): loading all the recipes from network.");
                return remoteDataSource.loadAllRecipes();
            }

            @Override
            protected void saveCallResult(@NonNull List<Recipe> loadedData) {
                // Save the result of the API response into the database.
                // FIXME: The param type is the data type returned from the API.
                localDataSource.saveAllRecipesDetails(loadedData);
                Timber.d("NetworkBoundResource saveCallResult(): %s recipes saved to database.", loadedData.size());
            }

            @Override
            protected void onFetchFailed() {
                // Ignored
                Timber.d("NetworkBoundResource onFetchFailed()");
            }

            @Override
            protected boolean shouldFetch(@Nullable List<DetailedRecipe> localData) {
                // Only fetch fresh data if it doesn't exist in database.
                // FIXME: The param type is the data type used locally.
                return localData == null || localData.size() == 0;
            }

            @NonNull
            @Override
            protected LiveData<List<DetailedRecipe>> loadFromDb() {
                // Get the cached data from the database.
                // FIXME: The return type is the data type used locally.
                Timber.d("NetworkBoundResource loadFromDb(): Getting all the detailed recipes from the database.");
                return localDataSource.getAllDetailedRecipes();
            }
        }.getAsLiveData();
    }

    public LiveData<Recipe> getSimpleRecipe(Long recipeId) {
        return localDataSource.getSimpleRecipe(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredients(Long recipeId) {
        return localDataSource.getIngredients(recipeId);
    }

    public LiveData<List<Step>> getSteps(Long recipeId) {
        return localDataSource.getSteps(recipeId);
    }

    public LiveData<List<Recipe>> getAllSimpleRecipes() {
        return localDataSource.getAllSimpleRecipes();
    }

    public DetailsLiveData getDetailsPairLiveData(Long recipeId) {
        return localDataSource.getDetailsPairLiveData(recipeId);
    }
}
