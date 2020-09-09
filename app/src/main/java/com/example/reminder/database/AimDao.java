package com.example.reminder.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AimDao
{
    @Query("SELECT * FROM aim ORDER BY originalDeadlineDate")
    LiveData<List<EachGoal>> loadAllGoals();

    @Insert
    void insertGoal(EachGoal eachGoal);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGoal(EachGoal eachGoal);

    @Delete
    void deleteGoal(EachGoal eachGoal);

    @Query("SELECT * FROM aim where id = :id")
    LiveData<EachGoal> loadGoalById(int id);
}
