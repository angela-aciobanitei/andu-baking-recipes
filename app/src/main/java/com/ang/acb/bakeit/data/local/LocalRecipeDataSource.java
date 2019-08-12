package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        Timber.d("Recipe [id=%s]: saving recipe details into the database.", recipe.getId());
    }

    private void saveIngredients (List<Ingredient> ingredientList, Long recipeId)  {
        for (Ingredient ingredient : ingredientList) {
            ingredient.setRecipeId(recipeId);
        }
        database.ingredientDao().insertRecipeIngredients(ingredientList);
        Timber.d("Recipe [id=%s]: %s ingredients inserted into the database.",
                recipeId, ingredientList.size());
    }

    private void saveSteps (List<Step> stepList, Long recipeId) {
        for (Step step : stepList) {
            step.setRecipeId(recipeId);
        }
        database.stepDao().insertRecipeSteps(stepList);
        Timber.d("Recipe [id=%s]: %s steps inserted into the database.",
                recipeId, stepList.size());
    }

    public LiveData<Recipe> getSimpleRecipe(Long recipeId) {
        Timber.d("Recipe [id=%s]: retrieving simple recipe from the database.", recipeId);
        return database.recipeDao().getSimpleRecipe(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredients(Long recipeId) {
        Timber.d("Recipe [id=%s]: retrieving ingredients from the database.", recipeId);
        return database.ingredientDao().getRecipeIngredients(recipeId);
    }

    public LiveData<List<Step>> getSteps(Long recipeId) {
        Timber.d("Recipe [id=%s]: retrieving steps from the database.", recipeId);
        return database.stepDao().getRecipeSteps(recipeId);
    }

    public LiveData<List<Recipe>> getAllSimpleRecipes(){
        Timber.d("Retrieving all simple recipes from the database. ");
        return database.recipeDao().getAllSimpleRecipes();
    }

    public LiveData<RecipeDetails> getRecipeDetails(Long recipeId) {
        // FIXME: Combine these 3 to get a LiveData<RecipeDetails> object
        MutableLiveData<Recipe> simpleRecipeLiveData = new MutableLiveData<>();
        MutableLiveData<List<Ingredient>> ingredientsLiveData = new MutableLiveData<>();
        MutableLiveData<List<Step>> stepsLiveData = new MutableLiveData<>();
        MutableLiveData<Long> recipeIdLiveData = new MutableLiveData<>();

        // Note: MediatorLiveData lets you add one or multiple sources of data
        // to a single LiveData observable. Note that the data is not combined
        // for you. MediatorLiveData simply takes care of notifications.
        // See: https://medium.com/androiddevelopers/livedata-beyond-the-viewmodel-
        // reactive-patterns-using-transformations-and-mediatorlivedata-fda520ba00b7
        MediatorLiveData<RecipeDetails> result = new MediatorLiveData<>();

        result.addSource(simpleRecipeLiveData, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe simpleRecipe) {
                if (simpleRecipe != null) {

                }
                simpleRecipeLiveData.setValue(simpleRecipe);
            }
        });

        result.addSource(ingredientsLiveData, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                if (ingredients != null) {

                }
                ingredientsLiveData.setValue(ingredients);
            }
        });

        result.addSource(stepsLiveData, new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                if (steps != null)  {

                }
                stepsLiveData.setValue(steps);
            }
        });
        Timber.d("Recipe [id=%s]: retrieving recipe details from the database.", recipeId);
        return result;
    }

    public LiveData<List<RecipeDetails>> getAllDetailedRecipes(){
        LiveData<List<Recipe>> simpleRecipesLiveData = database.recipeDao().getAllSimpleRecipes();
        MediatorLiveData<List<RecipeDetails>> result = new MediatorLiveData<>();

        result.addSource(simpleRecipesLiveData, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> simpleRecipeList) {
                List<RecipeDetails> detailedRecipeList = new ArrayList<>();

                if (simpleRecipeList != null) {
                    for (Recipe simpleRecipe : simpleRecipeList) {
                        RecipeDetails detailedRecipe = new RecipeDetails();
                        detailedRecipe.setRecipe(simpleRecipe);
                        detailedRecipe.setIngredients(getIngredients(simpleRecipe.getId()).getValue());
                        detailedRecipe.setSteps(getSteps(simpleRecipe.getId()).getValue());
                        detailedRecipeList.add(detailedRecipe);
                    }
                }

                result.setValue(detailedRecipeList);
            }
        });
        Timber.d("Retrieving all detailed recipes from the database. ");
        return result;
    }
}
