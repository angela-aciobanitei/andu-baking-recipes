package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.utils.AppExecutors;

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

    public void saveAllRecipes(List<Recipe> recipeList) {
        for (Recipe recipe : recipeList) {
            saveRecipeDetails(recipe);
        }
    }

    private void saveRecipeDetails(Recipe recipe) {
        database.recipeDao().insertRecipe(recipe);
        saveIngredients(recipe.getIngredients(), recipe.getId());
        saveSteps(recipe.getSteps(), recipe.getId());
    }

    private void saveIngredients (List<Ingredient> ingredientList, Integer recipeId)  {
        for (Ingredient ingredient : ingredientList) {
            ingredient.setRecipeId(recipeId);
        }
        database.ingredientDao().insertAllIngredients(ingredientList);
        Timber.d("%s ingredients inserted into the database.", ingredientList.size());
    }

    private void saveSteps (List<Step> stepList, Integer recipeId) {
        for (Step step : stepList) {
            step.setRecipeId(recipeId);
        }
        database.stepDao().insertAllSteps(stepList);
        Timber.d("%s steps inserted into the database.", stepList.size());
    }

    public LiveData<Recipe> getRecipe(Integer recipeId) {
        Timber.d("Retrieving recipe from the database. ");
        return database.recipeDao().getRecipe(recipeId);
    }

    public LiveData<List<Recipe>> getAllRecipes(){
        Timber.d("Retrieving all recipes from the database. ");
        return database.recipeDao().getAllRecipes();
    }

}
