package com.example.identiforge.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CompletedHabit {

    @PrimaryKey(autoGenerate = true)
    private  int id;

    private long timestamp;
    private int habitId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getHabitId() {
        return habitId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public String toString(){
        return "Id: " + id + " Tmsp: " + timestamp;
    }


}
