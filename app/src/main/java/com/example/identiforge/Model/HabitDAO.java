package com.example.identiforge.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface HabitDAO {

    @Insert
    void insertHabit(Habit habit);

    @Update
    void updateHabit(Habit habit);

    @Delete
    void deleteHabit(Habit habit);

    @RawQuery(observedEntities = Habit.class)
    LiveData<List<Habit>> getHabits(SupportSQLiteQuery habitDayQuery);

    //Raw query needed, there is function in the Helper to generate it
    @RawQuery
    List<Habit> getHabitsSynchronous(SupportSQLiteQuery habitDayQuery);

    @Query("DELETE FROM Habit WHERE identityId = :id")
    void deleteForgingHabits(int id);

}
