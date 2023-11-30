package com.example.identiforge.Logic;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.identiforge.Model.CompletedHabit;
import com.example.identiforge.Model.DateHelper;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.IdentiForgeDB;
import com.example.identiforge.Model.Identity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerImp extends Controller{

    private IdentiForgeDB db;
    private LiveData<List<Identity>> identities;
    private LiveData<List<Habit>> habits;

    public ControllerImp(Application app){
        db = IdentiForgeDB.get(app);
        identities = db.identityDAO().getIdentities();
        habits = db.habitDAO()
                .getHabits(Helper.getHabitDayQuery(DateHelper.getCurrentWekDay(DateHelper.getCurrentDate())));
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
    public void insertHabit(Habit habit) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.habitDAO().insertHabit(habit);
        });
    }

    @Override
    public void updateHabit(Habit habit) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.habitDAO().updateHabit(habit);
        });
    }

    @Override
    public void deleteHabit(Habit habit) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.habitDAO().deleteHabit(habit);
        });
    }

    @Override
    public LiveData<List<Habit>> getHabits() {
        return habits;
    }

    @Override
    public void refreshHabits(String day) {

    }

    @Override
    public void completeHabit(Habit habit, String day) {
        CompletedHabit ch = new CompletedHabit();
        ch.setHabitId(habit.getId());
        ch.setDay(day);
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            db.completedHabitDAO().insert(ch);
            db.identityDAO().addPoints(habit.getIdentityId(), habit.getPoints());
        });
    }

    @Override
    public List<CompletedHabit> getCompletedHabits(String day) {
        return db.completedHabitDAO().getCompletedHabits(day);
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
