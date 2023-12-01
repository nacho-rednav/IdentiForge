package com.example.identiforge.View.TasksView;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.identiforge.Logic.Controller;
import com.example.identiforge.Model.DateHelper;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Identity;
import com.example.identiforge.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends ViewModel {
    private LiveData<List<Task>> list;
    private Controller controller;
    public void init(Application app){
        controller = Controller.get(app);
        list = controller.getTasks(DateHelper.getCurrentDate());
    }
    public void insertTask(Task task, String day) {
        controller.insertTask(task, day);
    }
    public void updateTask(Task task, String day) {
        controller.updateTask(task, day);
    }
    public void deleteTask(Task task, String day) {
        controller.deleteTask(task, day);
    }
    public LiveData<List<Task>> getTasks() {
        return list;
    }
    public void refreshTasks(String day) {
        controller.refreshTasks(day);
    }
    public void completeTask(Task task, String day) {
        controller.completeTask(task, day);
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
