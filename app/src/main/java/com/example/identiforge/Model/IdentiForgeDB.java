package com.example.identiforge.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Identity.class, Habit.class, Task.class, CompletedHabit.class}, version = 1)
public abstract class IdentiForgeDB extends RoomDatabase {

    private static IdentiForgeDB instance;
    public abstract IdentityDAO identityDAO();
    public abstract HabitDAO habitDAO();
    public abstract TaskDAO taskDAO();
    public abstract CompletedHabitDAO completedHabitDAO();

    public static synchronized IdentiForgeDB get(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    IdentiForgeDB.class, "idf_database").build();
        }
        return instance;
    }

}
