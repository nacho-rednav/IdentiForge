package com.example.identiforge.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompletedHabitDAO {

    @Insert
    void insert(CompletedHabit ch);

    @Query("SELECT * FROM CompletedHabit WHERE timestamp >= :min AND timestamp <= :max")
    List<CompletedHabit> getCompletedHabits(long min, long max);
}
