package com.example.identiforge.Model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM Task WHERE timestamp = :timestamp")
    List<Task> getTasks(long timestamp);

    @Query("DELETE FROM Task WHERE identityId = :id")
    void deleteForgingTasks(int id);
}
