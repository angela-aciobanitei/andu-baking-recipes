package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.model.Recipe;;
import com.ang.acb.bakeit.utils.AppExecutors;

import java.util.ArrayList;
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

    public LiveData<List<Recipe>> loadAllRecipes(){
        // Initial live data source.
        LiveData<List<WholeRecipe>> wholeRecipesLiveData = database.recipeDao().loadWholeRecipes();

        // Final live data result. Note: MediatorLiveData<T> is a LiveData
        // subclass which may observe other LiveData objects and react on
        // OnChanged events from them.
        MediatorLiveData<List<Recipe>> result = new MediatorLiveData<>();
        result.addSource(wholeRecipesLiveData, wholeRecipes -> {
            List<Recipe> recipes = new ArrayList<>();
            if (wholeRecipes != null) {
                for (WholeRecipe wholeRecipe : wholeRecipes) {
                    wholeRecipe.recipe.setIngredients(wholeRecipe.ingredients);
                    wholeRecipe.recipe.setSteps(wholeRecipe.steps);
                    recipes.add(wholeRecipe.recipe);
                }
            }

            result.setValue(recipes);
        });

        return result;
    }

    public LiveData<WholeRecipe> getWholeRecipe(Integer recipeId) {
        return database.recipeDao().loadWholeRecipe(recipeId);
    }

    public WholeRecipe getRecipe(Integer recipeId) {
        return database.recipeDao().loadRecipe(recipeId);
    }

    public LiveData<List<WholeRecipe>> getWholeRecipes() {
        return database.recipeDao().loadWholeRecipes();
    }

    public LiveData<List<Step>> getRecipeSteps(Integer recipeId) {
        return database.recipeDao().loadRecipeSteps(recipeId);
    }

    public LiveData<List<Ingredient>> getRecipeIngredients(Integer recipeId) {
        return database.recipeDao().loadRecipeIngredients(recipeId);
    }
}
