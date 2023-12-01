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
import java.util.HashSet;
import java.util.List;

public class HabitViewModel extends ViewModel {

    private LiveData<List<Habit>> list;
    private Controller controller;
    public void init(Application app){
        controller = Controller.get(app);
        list = controller.getHabits();
    }
    public void insertHabit(Habit habit, String day) {
        controller.insertHabit(habit, day);
    }

    public void updateHabit(Habit habit, String day) {
        controller.updateHabit(habit, day);
    }

    public void deleteHabit(Habit habit, String day) {
        controller.deleteHabit(habit, day);
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

    public HashSet<Integer> getCompletedHabitsMap(String day) {
        HashSet<Integer> result = new HashSet<>();
        List<CompletedHabit> completedHabits = controller.getCompletedHabits(day);
        Log.d("fd", "VM List: " + completedHabits);
        if(completedHabits!= null){
            for(CompletedHabit ch : completedHabits){
                result.add(ch.getHabitId());
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
