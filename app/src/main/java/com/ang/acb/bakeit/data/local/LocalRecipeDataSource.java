package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Concrete implementation of a data source as a database.
 *
 * See: https://github.com/googlesamples/android-architecture/tree/todo-mvp/todoapp
 */
public class LocalRecipeDataSource {

    // For Singleton instantiation.
    private static volatile LocalRecipeDataSource sInstance;
    private final AppDatabase database;

    // Prevent direct instantiation.
    private LocalRecipeDataSource (AppDatabase database) {
        this.database = database;
    }

    // Returns the single instance of this class, creating it if necessary.
    public static LocalRecipeDataSource getInstance(AppDatabase database) {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new LocalRecipeDataSource(database);
                }
            }
        }
        return sInstance;
    }

    public void saveAllRecipesDetails(List<Recipe> recipeList) {
        for (Recipe recipe : recipeList) {
            saveRecipeDetails(recipe);
        }
        Timber.d("%s detailed recipes saved into the database.", recipeList.size());
    }

    private void saveRecipeDetails(Recipe recipe) {
        database.recipeDao().insertSimpleRecipe(recipe);
        saveIngredients(recipe.getIngredients(), recipe.getId());
        saveSteps(recipe.getSteps(), recipe.getId());
        Timber.d("Saving recipe details into the database. ");
    }

    private void saveIngredients (List<Ingredient> ingredientList, Long recipeId)  {
        for (Ingredient ingredient : ingredientList) {
            ingredient.setRecipeId(recipeId);
        }
        database.ingredientDao().insertRecipeIngredients(ingredientList);
        Timber.d("%s ingredients inserted into the database.", ingredientList.size());
    }

    private void saveSteps (List<Step> stepList, Long recipeId) {
        for (Step step : stepList) {
            step.setRecipeId(recipeId);
        }
        database.stepDao().insertRecipeSteps(stepList);
        Timber.d("%s steps inserted into the database.", stepList.size());
    }

    public LiveData<Recipe> getSimpleRecipe(Long recipeId) {
        Timber.d("Retrieving simple recipe from the database. ");
        return database.recipeDao().getSimpleRecipe(recipeId);
    }

    public LiveData<List<Recipe>> getAllSimpleRecipes(){
        Timber.d("Retrieving all simple recipes from the database. ");
        return database.recipeDao().getAllSimpleRecipes();
    }

    public LiveData<List<RecipeDetails>> getAllDetailedRecipes(){
        LiveData<List<Recipe>> simpleRecipesLiveData = database.recipeDao().getAllSimpleRecipes();
        MediatorLiveData<List<RecipeDetails>> result = new MediatorLiveData<>();

        // Call addSource(LiveData<S> source, Observer<S> onChanged)
        // to start listening the given source LiveData. The onChanged
        // observer will be called when source value was changed.
        result.addSource(simpleRecipesLiveData, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> simpleRecipeList) {
                List<RecipeDetails> detailedRecipeList = new ArrayList<>();

                if (simpleRecipeList != null) {
                    for (Recipe simpleRecipe : simpleRecipeList) {
                        RecipeDetails detailedRecipe = new RecipeDetails(
                                simpleRecipe,
                                simpleRecipe.getIngredients(),
                                simpleRecipe.getSteps());
                        detailedRecipeList.add(detailedRecipe);
                    }
                }

                result.setValue(detailedRecipeList);
            }
        });
        Timber.d("Retrieving all detailed recipes from the database. ");
        return result;
    }

    public LiveData<RecipeDetails> getRecipeDetails(Long recipeId) {
        LiveData<Recipe> simpleRecipeLiveData = database.recipeDao().getSimpleRecipe(recipeId);
        MediatorLiveData<RecipeDetails> result = new MediatorLiveData<>();

        result.addSource(simpleRecipeLiveData, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe simpleRecipe) {
                RecipeDetails detailedRecipe = null;
                if (simpleRecipe != null) {
                    detailedRecipe = new RecipeDetails(
                            simpleRecipe,
                            simpleRecipe.getIngredients(),
                            simpleRecipe.getSteps());
                }

                result.setValue(detailedRecipe);
            }
        });

        return null;
    }
}
