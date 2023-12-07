package com.example.identiforge.Logic;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.identiforge.Model.CompletedHabit;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Identity;
import com.example.identiforge.Model.Task;

import java.util.ArrayList;
import java.util.List;


/*
* Singleton interface to get an instance and access the Controller
* */
public abstract class Controller {

    private static Controller instance;

    public static Controller get(Application app){
        if(instance == null) instance = new ControllerImp(app);
        return instance;
    }

    /*
    * Function to insert a new identity
    * @param identity Identity : the identity to be stores
    * */
    public abstract void insertIdentity(Identity identity);

    /*
     * Function to update an identity
     * @param identity Identity : the identity to be updated
     * */
    public abstract void updateIdentity(Identity identity);

    /*
     * Function to delete an identity. It also deletes Habits and Tasks linked to the identity
     * @param identity Identity : the identity to be deleted
     * */
    public abstract void deleteIdentity(Identity identity);

    /*
     * Function to get all existing identities
     * @return LiveData<List<Identity>> : list of identities to be observed by the ViewModel
     * */
    public abstract LiveData<List<Identity>> getIdentities();

    /*
     * Function to an identity
     * @param int identityId : the id of the identity
     * @return Identity : the desired identity
     * */
    public abstract Identity getIdentity(int identityId);

    /*
     * Function level up an identity. It will change level and points accordingly
     * @param Identity identity : the identity to be leveled up
     * */
    public abstract void levelUp(Identity identity);

    /*
     * Function to get the id of an identity based on a title
     * @param String idTitle : the title of the desired identity
     * @return int : the id of the identity
     * */
    public abstract int getIdentityFromTitle(String idTitle);

    /*
     * Function to get the title of all identities
     * @return ArrayList<String> : list of all titles
     * */
    public abstract ArrayList<String> getIdentitiesAsStrings();

    /*
     * Function to insert a new habit
     * @param Habit habit : the habit to be inserted
     * */
    public abstract void insertHabit(Habit habit, String day);

    /*
     * Function to update a habit
     * @param Habit habit : the habit to be updated
     * */
    public abstract void updateHabit(Habit habit, String day);

    /*
     * Function to delete a habit
     * @param Habit habit : the habit to be deleted
     * */
    public abstract void deleteHabit(Habit habit, String day);

    /*
     * Function to get the stored list of habits
     * @return LiveData<List<Habit>> : the list of habits
     * */
    public abstract LiveData<List<Habit>> getHabits();

    /*
     * Function to tell the Controller to update its list with the habits of the given day
     * @param String day : the day for which you want the habits (day of the week)
     * */
    public abstract void refreshHabits(String day);

    /*
     * Function to complete a habit for a specific day. Day in format dd-mm-yyyy
     * @param Habit habit: the habit you have completed
     * @param String day : the day in which the habit was completed
     * */
    public abstract void completeHabit(Habit habit, String day);

    /*
     * Function to get the completed habits in range of 15 days before and 15 days after
     * the provided day
     * @param String day : desired day to get the range, in format dd-mm-yyyy
     * @return List<CompletedHabit> : the list of completed habits in that range
     * */
    public abstract List<CompletedHabit> getCompletedHabits(String day);

    /*
     * Function to delete a task
     * @param Task task : the task to be inserted
     * */
    public abstract void insertTask(Task task, String day);

    /*
     * Function to update a task
     * @param Task task : the task to be updated
     * */
    public abstract void updateTask(Task task, String day);

    /*
     * Function to delete a task
     * @param Task task : the task to be deleted
     * */
    public abstract void deleteTask(Task task, String day);

    /*
     * Function to get the stored list of tasks
     * @param LiveData<List<Task>>
     * */
    public abstract LiveData<List<Task>> getTasks();

    /*
     * Function to tell the Controller to update its list with the tasks of the given day
     * @param String day : the day for which you want the habits (dd-mm-yyyy)
     * */
    public abstract void refreshTasks(String day);

    /*
    * Function to complete a task, it will delete it
    * @param Task task : the completed task
    * @param String day : the day in which it has been completed (dd-mm-yyyy)
    * */
    public abstract void completeTask(Task task, String day);


}
