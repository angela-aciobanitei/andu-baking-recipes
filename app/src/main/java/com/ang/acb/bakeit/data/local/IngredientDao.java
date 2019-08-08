package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ang.acb.bakeit.data.model.Ingredient;

import java.util.List;

/**
 * Interface for database access on Ingredient related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllIngredients(List<Ingredient> ingredients);

    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> getAllIngredients();

    @Query("DELETE FROM ingredient")
    void deleteAllIngredients();
}
