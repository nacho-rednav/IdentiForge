package com.example.identiforge.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identiforge.Model.DateHelper;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Helper;
import com.example.identiforge.R;
import com.example.identiforge.View.HabitsView.HabitRVAdapter;
import com.example.identiforge.View.HabitsView.HabitViewModel;
import com.example.identiforge.View.IdentityView.IdentityListView;
import com.example.identiforge.View.IdentityView.IdentityViewModel;
import com.example.identiforge.View.ResultContracts.CreateHabitResultContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private HabitViewModel habitViewModel;
    private RecyclerView habitRV;
    private HabitRVAdapter habitAdapter;
    private ActivityResultLauncher<Pair<Habit, ArrayList<String>>> habitActivityResultLauncher;
    ArrayList<String> identitiesTitles;

    private String selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedDay = DateHelper.getCurrentDate();

        habitActivityResultLauncher = registerForActivityResult(new CreateHabitResultContract(),
                result -> {
                    if (Helper.isSafe(result))
                        onCreateHabitResult(result);
                });

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        habitViewModel.init(getApplication());
        habitRV = findViewById(R.id.main_recyclerViewHabit);
        habitRV.setLayoutManager(new LinearLayoutManager(this));
        habitRV.setHasFixedSize(true);
        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(() -> {
            habitAdapter = new HabitRVAdapter(this);
        });
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        habitRV.setAdapter(habitAdapter);
        initItemTouchHelper();
        habitViewModel.getHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                habitAdapter.submitList(habits);
            }
        });

        setDate();
        ImageView nextDayButt = findViewById(R.id.main_nextDate);
        nextDayButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prevDay = selectedDay;
                selectedDay = DateHelper.nextDay(selectedDay);
                try{
                    reload();
                }
                catch (InterruptedException ex){
                    Log.d("Error", ex.getMessage());
                    selectedDay = prevDay;
                }
            }
        });
        ImageView prevDayButt = findViewById(R.id.main_prevDate);
        prevDayButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prevDay = selectedDay;
                selectedDay = DateHelper.prevDay(selectedDay);
                try{
                    reload();
                }
                catch (InterruptedException ex){
                    Log.d("Error", ex.getMessage());
                    selectedDay = prevDay;
                }
            }
        });


        ImageView addHabitBut = findViewById(R.id.main_habitButt);
        addHabitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
                service.execute(() -> {
                    Looper.prepare();
                    identitiesTitles = habitViewModel.getIdentityList();
                    if (identitiesTitles == null || identitiesTitles.isEmpty()) {
                        Toast.makeText(MainActivity.this, "You have to create an Identity first!", Toast.LENGTH_LONG).show();
                    } else {
                        Pair<Habit, ArrayList<String>> input = new Pair<>(null, identitiesTitles);
                        habitActivityResultLauncher.launch(input);
                    }
                });
            }
        });
        ImageView addIdentityButt = findViewById(R.id.main_identityButt);
        addIdentityButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, IdentityListView.class);
                startActivity(i);
            }
        });
    }

    private void setDate() {
        TextView dayTV = findViewById(R.id.main_date);
        dayTV.setText(selectedDay + "\n" + DateHelper.getCurrentWekDay(selectedDay));
    }

    private void onCreateHabitResult(Pair<Pair<Habit, String>, Boolean> result) {
        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(() -> {
            Looper.prepare();
            try{
                if (Helper.isSafe(result) && Helper.isSafe(result.first) && Helper.isSafe(result.second)
                        && Helper.isSafe(result.first.first) && Helper.isSafe(result.first.second)) {

                    int identityId = habitViewModel.getIdentityFromTitle(result.first.second);
                    result.first.first.setIdentityId(identityId);

                    if (result.second) {
                        habitViewModel.updateHabit(result.first.first);
                    } else {
                        habitViewModel.insertHabit(result.first.first);
                    }
                } else {
                    Toast.makeText(this, "Something failed", Toast.LENGTH_SHORT);
                }
            }
            catch(Exception ex){
                Log.d("Error", ex.getMessage());
                Toast.makeText(this, "Something failed, the Habit was not saved", Toast.LENGTH_SHORT);
            }

        });
    }

    public void completeHabit(Habit h) {
        Log.d("fd", "OHOO: " + h.getTile() +" " + h.getId());
        habitViewModel.completeHabit(h, selectedDay);
    }
    public void deleteHabit(Habit h){habitViewModel.deleteHabit(h);}

    private void reload() throws InterruptedException {
        setDate();
        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(() -> {
            habitAdapter.setHabitIdMap(getCompletedHabitMap());
        });

        habitViewModel.refreshHabits(selectedDay);
    }

    public void launchEditHabit(Habit habit) {
        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(() -> {
            Looper.prepare();
            try{
                identitiesTitles = habitViewModel.getIdentityList();
                if (identitiesTitles == null || identitiesTitles.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Something failed!", Toast.LENGTH_LONG).show();
                } else {
                    int idPos = identitiesTitles.indexOf(habitViewModel.getIdentityString(habit.getIdentityId()));
                    habit.setIdentityId(idPos);
                    Pair<Habit, ArrayList<String>> input = new Pair<>(habit, identitiesTitles);
                    habitActivityResultLauncher.launch(input);
                }
            }
            catch(Exception ex){
                Log.d("Error", ex.getMessage());
            }

        });
    }

    public HashSet<Integer> getCompletedHabitMap() {
        return habitViewModel.getCompletedHabitsMap(selectedDay);
    }

    private void initItemTouchHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteHabit(habitAdapter.getHabitOn(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(habitRV);

        //Tasks touch helper
    }








    class PriorityThreadFactory implements ThreadFactory {
        private final int priority;

        public PriorityThreadFactory(int priority) {
            this.priority = priority;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(priority);
            return thread;
        }
    }
}