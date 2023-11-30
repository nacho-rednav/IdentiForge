package com.example.identiforge.View.HabitsView;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.identiforge.Logic.Controller;
import com.example.identiforge.Model.CompletedHabit;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HabitViewModel extends ViewModel {

    private LiveData<List<Habit>> list;
    private Controller controller;
    public void init(Application app){
        controller = Controller.get(app);
        list = controller.getHabits();
    }
    public void insertHabit(Habit habit) {
        controller.insertHabit(habit);
    }

    public void updateHabit(Habit habit) {
        controller.updateHabit(habit);
    }

    public void deleteHabit(Habit habit) {
        controller.deleteHabit(habit);
    }

    public LiveData<List<Habit>> getHabits() {
        return list;
    }

    public void refreshHabits(String day) {
        controller.refreshHabits(day);
    }

    public void completeHabit(Habit habit, String day) {
        controller.completeHabit(habit, day);
    }

    public HashMap<Integer, Boolean> getCompletedHabitsMap(String day) {
        HashMap<Integer, Boolean> result = new HashMap<>();
        List<CompletedHabit> completedHabits = controller.getCompletedHabits(day);

        if(completedHabits!= null){
            for(CompletedHabit ch : completedHabits){
                result.put(ch.getHabitId(), true);
            }
        }

        return result;
    }

    public int getIdentityFromTitle(String idTitle) {
        return controller.getIdentityFromTitle(idTitle);
    }

    public ArrayList<String> getIdentityList() {
        return controller.getIdentitiesAsStrings();
    }

    public String getIdentityString(int identityId) {
        Identity idTitle = controller.getIdentity(identityId);
        return idTitle.getTitle();
    }
}