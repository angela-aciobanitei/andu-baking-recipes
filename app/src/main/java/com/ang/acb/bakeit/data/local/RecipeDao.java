package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;

import java.util.List;

/**
 * Interface for database access on Recipe related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public abstract class RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRecipe(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSimpleRecipes(List<Recipe> recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertIngredients(List<Ingredient> ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSteps(List<Step> steps);

    public void insertAllRecipes(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {

            List<Ingredient> ingredients = recipe.getIngredients();
            for (Ingredient ingredient : ingredients) {
                ingredient.setRecipeId(recipe.getId());
            }
            insertIngredients(ingredients);

            List<Step> steps = recipe.getSteps();
            for (Step step : steps) {
                step.setRecipeId(recipe.getId());
            }
            insertSteps(steps);
        }
        insertSimpleRecipes(recipes);
    }

    @Transaction
    @Query("SELECT * FROM ingredients where recipe_id= :recipeId")
    public abstract LiveData<List<Ingredient>> loadIngredients(Integer recipeId);

    @Transaction
    @Query("SELECT * FROM steps where recipe_id= :recipeId")
    public abstract LiveData<List<Step>> loadSteps(Integer recipeId);

    @Transaction
    @Query("SELECT * FROM recipes WHERE id= :recipeId")
    public abstract LiveData<WholeRecipe> loadWholeRecipe(Integer recipeId);

    @Transaction
    @Query("SELECT * FROM recipes")
    public abstract LiveData<List<WholeRecipe>> loadWholeRecipes();

}
