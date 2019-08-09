package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

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

    private void saveIngredients (List<Ingredient> ingredientList, Long recipeId)  {
        for (Ingredient ingredient : ingredientList) {
            ingredient.setRecipeId(recipeId);
        }
        database.ingredientDao().insertAllIngredients(ingredientList);
        Timber.d("%s ingredients inserted into the database.", ingredientList.size());
    }

    private void saveSteps (List<Step> stepList, Long recipeId) {
        for (Step step : stepList) {
            step.setRecipeId(recipeId);
        }
        database.stepDao().insertAllSteps(stepList);
        Timber.d("%s steps inserted into the database.", stepList.size());
    }

    public LiveData<Recipe> getRecipe(Long recipeId) {
        Timber.d("Retrieving recipe from the database. ");
        return database.recipeDao().getRecipe(recipeId);
    }

    public LiveData<List<Recipe>> getAllRecipes(){
        Timber.d("Retrieving all recipes from the database. ");
        return database.recipeDao().getAllRecipes();
    }

    public LiveData<List<RecipeDetails>> getAllRecipesDetailed(){

        LiveData<List<Recipe>> recipesLiveData = database.recipeDao().getAllRecipes();
        MediatorLiveData<List<RecipeDetails>> detailedRecipesLiveData = new MediatorLiveData<>();
        detailedRecipesLiveData.addSource(recipesLiveData, recipeList -> {
            List<RecipeDetails> detailedRecipeList = new ArrayList<>();

            if (recipeList != null) {
                for (Recipe recipe : recipeList) {
                    RecipeDetails recipeDetailsItem = new RecipeDetails(
                            recipe,
                            recipe.getIngredients(),
                            recipe.getSteps());
                    detailedRecipeList.add(recipeDetailsItem);
                }
            }

            detailedRecipesLiveData.setValue(detailedRecipeList);
        });

        return detailedRecipesLiveData;
    }

    public LiveData<Resource<RecipeDetails>> getRecipeDetails(Long recipeId) {
        // FIXME
        MediatorLiveData<Resource<RecipeDetails>> detailedRecipeLiveData = new MediatorLiveData<>();
        LiveData<Recipe> recipeLiveData = database.recipeDao().getRecipe(recipeId);
        return null;
    }
}
