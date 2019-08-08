package com.ang.acb.bakeit.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ang.acb.bakeit.data.model.Step;

import java.util.List;

/**
 * Interface for database access on Ingredient related operations.
 *
 * See: https://medium.com/androiddevelopers/7-steps-to-room-27a5fe5f99b2
 * See: https://medium.com/androiddevelopers/7-pro-tips-for-room-fbadea4bfbd1
 */
@Dao
public interface StepDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllSteps(List<Step> steps);

    @Query("SELECT * FROM step")
    LiveData<List<Step>> getAllSteps();

    @Query("DELETE FROM step")
    void deleteAllSteps();
}
