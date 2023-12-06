package com.example.identiforge.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identiforge.Model.DateHelper;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.Task;
import com.example.identiforge.R;
import com.example.identiforge.View.HabitsView.HabitRVAdapter;
import com.example.identiforge.View.HabitsView.HabitViewModel;
import com.example.identiforge.View.IdentityView.IdentityListView;
import com.example.identiforge.View.ResultContracts.CreateHabitResultContract;
import com.example.identiforge.View.ResultContracts.CreateTaskResultContract;
import com.example.identiforge.View.TasksView.TaskRVAdapter;
import com.example.identiforge.View.TasksView.TaskViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MainActivity extends AppCompatActivity {

    private HabitViewModel habitViewModel;
    private RecyclerView habitRV;
    private HabitRVAdapter habitAdapter;
    private ActivityResultLauncher<Pair<Habit, ArrayList<String>>> habitActivityResultLauncher;
    private ActivityResultLauncher<Pair<Task, ArrayList<String>>> taskActivityResultLauncher;
    ArrayList<String> identitiesTitles;
    private TaskViewModel taskViewModel;
    private RecyclerView taskRV;
    private TaskRVAdapter taskAdapter;
    private HashSet<Pair<Integer, Long>> completedHabits;
    private String selectedDay;
    private long prevTimeStamp;
    private long postTimeStamp;

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedDay = DateHelper.getCurrentDate();
        setDayLimits();


        habitActivityResultLauncher = registerForActivityResult(new CreateHabitResultContract(),
                result -> {
                    if (Helper.isSafe(result))
                        onCreateHabitResult(result);
                });
        taskActivityResultLauncher = registerForActivityResult(new CreateTaskResultContract(),
                result -> {
                    if(Helper.isSafe(result))
                        onCreateTaskResult(result);
                });

        habitViewModel = new ViewModelProvider(this).get(HabitViewModel.class);
        habitViewModel.init(getApplication());
        habitRV = findViewById(R.id.main_recyclerViewHabit);
        habitRV.setLayoutManager(new LinearLayoutManager(this));
        habitRV.setHasFixedSize(false);
        habitAdapter = new HabitRVAdapter(this);
        habitRV.setAdapter(habitAdapter);
        habitViewModel.getHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                seeChecked(habits);
                //habitAdapter.submitList(habits);
            }
        });

        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(new Runnable() {
            @Override
            public void run() {
                loadSetFrom();
            }
        });

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.init(getApplication());
        taskRV = findViewById(R.id.main_recyclerViewTask);
        taskRV.setLayoutManager(new LinearLayoutManager(this));
        taskRV.setHasFixedSize(false);
        taskAdapter = new TaskRVAdapter(this);
        taskRV.setAdapter(taskAdapter);
        taskViewModel.getTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.submitList(tasks);

            }
        });

        initItemTouchHelper();

        setDate();
        ImageView nextDayButt = findViewById(R.id.main_nextDate);
        nextDayButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDay = DateHelper.addDays(selectedDay, 1);
                if(DateHelper.getTimeStamp(selectedDay) > postTimeStamp){
                    setDayLimits();
                    loadSetFrom();
                }
                reload();
            }
        });
        ImageView prevDayButt = findViewById(R.id.main_prevDate);
        prevDayButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDay = DateHelper.addDays(selectedDay, -1);
                if(DateHelper.getTimeStamp(selectedDay) < prevTimeStamp){
                    setDayLimits();
                    loadSetFrom();
                }
                reload();
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
        ImageView addTaskButt = findViewById(R.id.main_taskButt);
        addTaskButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
                service.execute(() -> {
                    Looper.prepare();
                    identitiesTitles = taskViewModel.getIdentityList();
                    if (identitiesTitles == null || identitiesTitles.isEmpty()) {
                        Toast.makeText(MainActivity.this, "You have to create an Identity first!", Toast.LENGTH_LONG).show();
                    } else {
                        Pair<Task, ArrayList<String>> input = new Pair<>(null, identitiesTitles);
                        taskActivityResultLauncher.launch(input);
                    }
                });
            }
        });
    }

    private void setDayLimits() {
        prevTimeStamp = DateHelper.getTimeStamp(DateHelper.addDays(selectedDay, -15));
        postTimeStamp = DateHelper.getTimeStamp(DateHelper.addDays(selectedDay, 15));
    }

    private void loadSetFrom() {
        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(() -> {
            completedHabits = habitViewModel.loadSetFrom(selectedDay);
            seeChecked(habitAdapter.getCurrentList());
        });
    }

    private void seeChecked(List<Habit> list) {
        long tmsp = DateHelper.getTimeStamp(selectedDay);
        for(Habit h : list){
            if(completedHabits.contains(new Pair<>(h.getId(), tmsp))){
                h.setDone(true);
            }
        }
        habitAdapter.submitList(list);
        //habitRV.setAdapter(null);
        //habitRV.setAdapter(habitAdapter);
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
                        habitViewModel.updateHabit(result.first.first, selectedDay);
                    } else {
                        habitViewModel.insertHabit(result.first.first, selectedDay);
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

    private void onCreateTaskResult(Pair<Pair<Task, String>, Boolean> result){
        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(() -> {
            Looper.prepare();
            try{
                if (Helper.isSafe(result) && Helper.isSafe(result.first) && Helper.isSafe(result.second)
                        && Helper.isSafe(result.first.first) && Helper.isSafe(result.first.second)) {

                    int identityId = taskViewModel.getIdentityFromTitle(result.first.second);
                    result.first.first.setIdentityId(identityId);

                    if (result.second) {
                        taskViewModel.updateTask(result.first.first, selectedDay);
                    } else {
                        taskViewModel.insertTask(result.first.first, selectedDay);
                    }
                } else {
                    Toast.makeText(this, "Something failed", Toast.LENGTH_SHORT);
                }
            }
            catch(Exception ex){
                Log.d("Error", ex.getMessage());
                Toast.makeText(this, "Something failed, the Task was not saved", Toast.LENGTH_SHORT);
            }

        });
    }

    public void completeHabit(Habit h) {
        habitViewModel.completeHabit(h, selectedDay);
        completedHabits.add(new Pair<>(h.getId(), DateHelper.getTimeStamp(selectedDay)));
    }
    public void completeTask(Task t) {
        taskViewModel.completeTask(t, selectedDay);
    }
    public void deleteHabit(Habit h){habitViewModel.deleteHabit(h, selectedDay);}
    public void deleteTask(Task t){taskViewModel.deleteTask(t, selectedDay);}


    private void reload() {
        setDate();
        habitViewModel.refreshHabits(selectedDay);
        taskViewModel.refreshTasks(selectedDay);
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

    public void launchEditTask(Task task) {
        ExecutorService service = Executors.newSingleThreadExecutor(new PriorityThreadFactory(Thread.MAX_PRIORITY));
        service.execute(() -> {
            Looper.prepare();
            try{
                identitiesTitles = taskViewModel.getIdentityList();
                if (identitiesTitles == null || identitiesTitles.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Something failed!", Toast.LENGTH_LONG).show();
                } else {
                    int idPos = identitiesTitles.indexOf(taskViewModel.getIdentityString(task.getIdentityId()));
                    task.setIdentityId(idPos);
                    task.setDay(selectedDay);
                    Pair<Task, ArrayList<String>> input = new Pair<>(task, identitiesTitles);
                    taskActivityResultLauncher.launch(input);
                }
            }
            catch(Exception ex){
                Log.d("Error", ex.getMessage());
            }

        });
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteTask(taskAdapter.getTaskOn(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(taskRV);
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