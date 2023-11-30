package com.example.identiforge.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CompletedHabit {

    @PrimaryKey(autoGenerate = true)
    private  int id;

    private String day;
    private int habitId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getHabitId() {
        return habitId;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public String toString(){
        return "Id: " + id + " Date: " + day;
    }
}
