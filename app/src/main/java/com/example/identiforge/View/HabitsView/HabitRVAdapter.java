package com.example.identiforge.View.HabitsView;

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
import com.example.identiforge.Model.Helper;
import com.example.identiforge.R;
import com.example.identiforge.View.MainActivity;

public class HabitRVAdapter extends ListAdapter<Habit, HabitRVAdapter.ViewHolder> {

    private MainActivity parent;
    public HabitRVAdapter(MainActivity parent) {
        super(DIFF_CALLBACK);
        this.parent = parent;
    }

    private static final DiffUtil.ItemCallback<Habit> DIFF_CALLBACK = new DiffUtil.ItemCallback<Habit>() {
        @Override
        public boolean areItemsTheSame(Habit oldItem, Habit newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Habit oldItem, Habit newItem) {
            // below line is to check the course name, description and course duration.
            if(!(Helper.isSafe(oldItem) &&  Helper.isSafe(newItem)))
                return false;
            return oldItem.getPoints() == newItem.getPoints() && oldItem.getTile().equals(newItem.getTile()) &&
                    oldItem.getIdentityId() == newItem.getIdentityId() &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.isMonday() == newItem.isMonday() && oldItem.isTuesday() == newItem.isTuesday() &&
                    oldItem.isWednesday() == newItem.isWednesday() && oldItem.isThursday() == newItem.isThursday() &&
                    oldItem.isFriday() == newItem.isFriday() && oldItem.isSaturday() == newItem.isSaturday() &&
                    oldItem.isSunday() == newItem.isSunday() && oldItem.isDone() == newItem.isDone();
        }
    };
    @NonNull
    @Override
    public HabitRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_task_row, parent, false);

        return new HabitRVAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitRVAdapter.ViewHolder holder, int position) {
        Habit habit = getItem(position);

        holder.titleTV.setText(habit.getTile());
        holder.pointsTV.setText("Points: " + habit.getPoints());

        holder.imageCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!habit.isDone()){
                    parent.completeHabit(habit);
                    holder.imageCheck.setImageResource(R.drawable.checked);
                    habit.setDone(true);
                    Toast.makeText(parent, "Great, you earned " + habit.getPoints() + " points!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(parent, "Yep, you completed this habit today!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.launchEditHabit(habit);
            }
        });

        if(habit.isDone()){
            holder.imageCheck.setImageResource(R.drawable.checked);
        }
        else{
            holder.imageCheck.setImageResource(R.drawable.unchecked);
        }
    }

    public Habit getHabitOn(int adapterPosition) {
        return getItem(adapterPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTV, pointsTV;
        ImageView imageCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.habitRow_title);
            pointsTV = itemView.findViewById(R.id.habitRow_points);
            imageCheck = itemView.findViewById(R.id.habitRow_checkImg);
        }
    }
}
