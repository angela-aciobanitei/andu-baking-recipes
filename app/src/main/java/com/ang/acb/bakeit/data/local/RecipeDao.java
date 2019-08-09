package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ang.acb.bakeit.data.model.Recipe;

import java.util.List;

/**
 * Interface for database access on Recipe related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSimpleRecipe(Recipe recipe);

    @Transaction
    @Query("SELECT * FROM recipe WHERE recipe.id= :recipeId")
    LiveData<Recipe> getSimpleRecipe(Long recipeId);

    @Transaction
    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getAllSimpleRecipes();
}
