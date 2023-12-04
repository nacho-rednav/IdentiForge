package com.example.identiforge.View.TasksView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Task;
import com.example.identiforge.R;
import com.example.identiforge.View.HabitsView.HabitRVAdapter;
import com.example.identiforge.View.MainActivity;

public class TaskRVAdapter extends ListAdapter<Task, TaskRVAdapter.ViewHolder> {

    private MainActivity parent;
    public TaskRVAdapter(MainActivity parent) {
        super(DIFF_CALLBACK);
        this.parent = parent;
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(Task oldItem, Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Task oldItem, Task newItem) {
            // below line is to check the course name, description and course duration.
            if(oldItem.getDay() == null) Log.d("", "Day");
            else if(oldItem.getTitle() == null) Log.d("", "Titi");
            else if(oldItem.getDescription() == null) Log.d("", "Desc");

            return oldItem.getPoints() == newItem.getPoints() &&
                    oldItem.getIdentityId() == newItem.getIdentityId() &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getDay().equals(newItem.getDay()) &&
                    oldItem.getTitle().equals(newItem.getTitle());
        }
    };
    @NonNull
    @Override
    public TaskRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_task_row, parent, false);

        return new TaskRVAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRVAdapter.ViewHolder holder, int position) {
        Task task = getItem(position);

        holder.titleTV.setText(task.getTitle());
        holder.pointsTV.setText("Points: " + task.getPoints());

        holder.imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.completeTask(task);
                Toast.makeText(parent, "Great, you earned " + task.getPoints() + " points!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.launchEditTask(task);
            }
        });
    }
    public Task getTaskOn(int adapterPosition) {
        return getItem(adapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTV, pointsTV;
        ImageView imageCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TextView header = itemView.findViewById(R.id.habitRow_habit);
            header.setText("Task");
            titleTV = itemView.findViewById(R.id.habitRow_title);
            pointsTV = itemView.findViewById(R.id.habitRow_points);
            imageCheck = itemView.findViewById(R.id.habitRow_checkImg);
        }
    }
}
