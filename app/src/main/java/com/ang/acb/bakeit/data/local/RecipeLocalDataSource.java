package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.Recipe;;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Concrete implementation of a data source as a database.
 *
 * See: https://github.com/googlesamples/android-architecture/tree/todo-mvp/todoapp
 */
@Singleton
public class RecipeLocalDataSource {

    private final AppDatabase database;

    @Inject
    public RecipeLocalDataSource(AppDatabase database) {
        this.database = database;
    }

    public void saveAllRecipes(List<Recipe> recipes) {
        database.recipeDao().insertAllRecipes(recipes);
        Timber.d("%s detailed recipes saved into the database.", recipes.size());
    }

    public LiveData<List<RecipeDetails>> getRecipeDetailsList(){
        return database.recipeDao().getRecipeDetailsList();
    }

    public LiveData<RecipeDetails> getRecipeDetails(Integer recipeId) {
        return database.recipeDao().getRecipeDetails(recipeId);
    }

    public RecipeDetails getRecipeDetailsSync(Integer recipeId) {
        return database.recipeDao().getRecipeDetailsSync(recipeId);
    }

    public LiveData<List<Step>> getRecipeSteps(Integer recipeId) {
        return database.recipeDao().getRecipeSteps(recipeId);
    }

    public LiveData<List<Ingredient>> getRecipeIngredients(Integer recipeId) {
        return database.recipeDao().getRecipeIngredients(recipeId);
    }
}
