package com.example.identiforge.Logic;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.identiforge.Model.CompletedHabit;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Identity;

import java.util.ArrayList;
import java.util.List;

public abstract class Controller {

    private static Controller instance;

    public static Controller get(Application app){
        if(instance == null) instance = new ControllerImp(app);
        return instance;
    }

    public abstract void insertIdentity(Identity identity);
    public abstract void updateIdentity(Identity identity);
    public abstract void deleteIdentity(Identity identity);
    public abstract LiveData<List<Identity>> getIdentities();
    public abstract Identity getIdentity(int identityId);
    public abstract void levelUp(Identity identity);

    public abstract void insertHabit(Habit habit);
    public abstract void updateHabit(Habit habit);
    public abstract void deleteHabit(Habit habit);
    public abstract LiveData<List<Habit>> getHabits();
    public abstract void refreshHabits(String day);
    public abstract void completeHabit(Habit habit, String day);
    public abstract List<CompletedHabit> getCompletedHabits(String day);
    public abstract int getIdentityFromTitle(String idTitle);

    public abstract ArrayList<String> getIdentitiesAsStrings();

}
