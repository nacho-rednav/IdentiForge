package com.example.identiforge.Logic;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.identiforge.Model.CompletedHabit;
import com.example.identiforge.Model.DateHelper;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.IdentiForgeDB;
import com.example.identiforge.Model.Identity;
import com.example.identiforge.Model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* The controller is the repository of the app
* It stores Live Data of identities and Mutable Live Data of Tasks and Habits
* Mutable Live Data is used because we need to change the whole list stored inside the
* Live Data when the day is changed, for which we need the post method
*
* The Controller receives days (dd-mm-yyyy) from the Views, but the DAO works with timestamps
* There is a function in the DateHelper to do the transformation
* */
public class ControllerImp extends Controller{

    private IdentiForgeDB db;
    private LiveData<List<Identity>> identities;
    private MutableLiveData<List<Habit>> mutableHabits;
    private MutableLiveData<List<Task>> mutableTasks;

    public ControllerImp(Application app){
        db = IdentiForgeDB.get(app);

        identities = db.identityDAO().getIdentities();

        mutableHabits = new MutableLiveData<>();
        mutableTasks = new MutableLiveData<>();

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            mutableHabits.postValue(db.habitDAO()
                    .getHabitsSynchronous(Helper.getHabitDayQuery(DateHelper.getCurrentWekDay(DateHelper.getCurrentDate()))));

            mutableTasks.postValue(db.taskDAO().getTasks(DateHelper.getTimeStamp(DateHelper.getCurrentDate())));
        });
    }

    @Override
    public void insertIdentity(Identity identity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.identityDAO().insertIdentity(identity);
        });
    }

    @Override
    public void updateIdentity(Identity identity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.identityDAO().updateIdentity(identity);
        });
    }

    @Override
    public void deleteIdentity(Identity identity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.identityDAO().deleteIdentity(identity);
            db.taskDAO().deleteForgingTasks(identity.getId());
            db.habitDAO().deleteForgingHabits(identity.getId());
        });
    }

    @Override
    public LiveData<List<Identity>> getIdentities() {
        return identities;
    }

    @Override
    public Identity getIdentity(int identityId) {
        return db.identityDAO().getIdentity(identityId);
    }

    @Override
    public void levelUp(Identity identity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.identityDAO().levelUp(identity.getId());
        });
    }

    @Override
    public void insertHabit(Habit habit, String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.habitDAO().insertHabit(habit);
            List<Habit> list = db.habitDAO().getHabitsSynchronous(Helper.getHabitDayQuery(DateHelper.getCurrentWekDay(day)));
            mutableHabits.postValue(list);
        });
    }

    @Override
    public void updateHabit(Habit habit, String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.habitDAO().updateHabit(habit);
            List<Habit> list = db.habitDAO().getHabitsSynchronous(Helper.getHabitDayQuery(DateHelper.getCurrentWekDay(day)));
            mutableHabits.postValue(list);
        });
    }

    @Override
    public void deleteHabit(Habit habit, String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.habitDAO().deleteHabit(habit);
            List<Habit> list = db.habitDAO().getHabitsSynchronous(Helper.getHabitDayQuery(DateHelper.getCurrentWekDay(day)));
            mutableHabits.postValue(list);
        });
    }

    @Override
    public LiveData<List<Habit>> getHabits() {
        return mutableHabits;
    }

    @Override
    public void refreshHabits(String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            List<Habit> list = db.habitDAO().getHabitsSynchronous(Helper.getHabitDayQuery(DateHelper.getCurrentWekDay(day)));
            mutableHabits.postValue(list);
        });
    }

    @Override
    public void completeHabit(Habit habit, String day) {
        CompletedHabit ch = new CompletedHabit();
        ch.setHabitId(habit.getId());
        ch.setTimestamp(DateHelper.getTimeStamp(day));
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.completedHabitDAO().insert(ch);
            db.identityDAO().addPoints(habit.getIdentityId(), habit.getPoints());
        });
    }

    @Override
    public List<CompletedHabit> getCompletedHabits(String day) {
        long min = DateHelper.getTimeStamp(DateHelper.addDays(day, -15));
        long max = DateHelper.getTimeStamp(DateHelper.addDays(day, 15));
        return db.completedHabitDAO().getCompletedHabits(min, max);
    }

    @Override
    public void insertTask(Task task, String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Log.d("D","TASK dia: " + task.getDay());
            Log.d("D","dia: " + day);
            db.taskDAO().insertTask(task);
            List<Task> list = db.taskDAO().getTasks(DateHelper.getTimeStamp(day));
            mutableTasks.postValue(list);
        });
    }

    @Override
    public void updateTask(Task task, String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Log.d("D", "UPDATE: " + task.getDay());
            db.taskDAO().updateTask(task);
            List<Task> list = db.taskDAO().getTasks(DateHelper.getTimeStamp(day));
            mutableTasks.postValue(list);
        });
    }

    @Override
    public void deleteTask(Task task, String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.taskDAO().deleteTask(task);
            List<Task> list = db.taskDAO().getTasks(DateHelper.getTimeStamp(day));
            mutableTasks.postValue(list);
        });
    }

    @Override
    public LiveData<List<Task>> getTasks() {
        return mutableTasks;
    }

    @Override
    public void refreshTasks(String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            List<Task> list = db.taskDAO().getTasks(DateHelper.getTimeStamp(day));
            mutableTasks.postValue(list);
        });
    }

    @Override
    public void completeTask(Task task, String day) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.taskDAO().deleteTask(task);
            db.identityDAO().addPoints(task.getIdentityId(), task.getPoints());
            List<Task> list = db.taskDAO().getTasks(DateHelper.getTimeStamp(day));
            mutableTasks.postValue(list);
        });

    }

    @Override
    public int getIdentityFromTitle(String idTitle) {
        int res = 0;
        List<Identity> list = db.identityDAO().getSynchronousIdentities();
        for(Identity i : list){
            if(i.getTitle().equals(idTitle)){
                res = i.getId();
                break;
            }
        }
        return res;
    }

    @Override
    public ArrayList<String> getIdentitiesAsStrings() {
        List<Identity> list = db.identityDAO().getSynchronousIdentities();
        ArrayList<String> res = new ArrayList<>();
        if(list != null){
            for(Identity i : list){
                res.add(i.getTitle());
            }
        }
        return res;
    }
}
