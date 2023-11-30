package com.example.identiforge.View.HabitsView;

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
import com.example.identiforge.Model.Identity;
import com.example.identiforge.R;
import com.example.identiforge.View.IdentityView.IdentityListView;
import com.example.identiforge.View.IdentityView.IdentityRVAdapter;
import com.example.identiforge.View.MainActivity;

import java.util.HashMap;

public class HabitRVAdapter extends ListAdapter<Habit, HabitRVAdapter.ViewHolder> {

    private MainActivity parent;
    private HashMap<Integer, Boolean> completedHabitIdMap;
    public HabitRVAdapter(MainActivity parent) {
        super(DIFF_CALLBACK);
        this.parent = parent;
        completedHabitIdMap = parent.getCompletedHabitMap();
    }

    public void refreshHabitIdMap(){completedHabitIdMap = parent.getCompletedHabitMap();}
    private static final DiffUtil.ItemCallback<Habit> DIFF_CALLBACK = new DiffUtil.ItemCallback<Habit>() {
        @Override
        public boolean areItemsTheSame(Habit oldItem, Habit newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Habit oldItem, Habit newItem) {
            // below line is to check the course name, description and course duration.
            return oldItem.getPoints() == newItem.getPoints() &&
                    oldItem.getIdentityId() == newItem.getIdentityId() &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.isMonday() == newItem.isMonday() && oldItem.isTuesday() == newItem.isTuesday() &&
                    oldItem.isWednesday() == newItem.isWednesday() && oldItem.isThursday() == newItem.isThursday() &&
                    oldItem.isFriday() == newItem.isFriday() && oldItem.isSaturday() == newItem.isSaturday() &&
                    oldItem.isSunday() == newItem.isSunday();
        }
    };
    @NonNull
    @Override
    public HabitRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_row, parent, false);

        return new HabitRVAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitRVAdapter.ViewHolder holder, int position) {
        Habit habit = getItem(position);

        holder.titleTV.setText(habit.getTile());
        holder.pointsTV.setText("Points: " + habit.getPoints());
        if(completedHabitIdMap.get(habit.getId()) != null &&
                completedHabitIdMap.get(habit.getId())){
            holder.imageCheck.setImageResource(R.drawable.checked);
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

            imageCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Habit h = getItem(getAdapterPosition());
                    if(completedHabitIdMap.get(h.getId()) == null || !completedHabitIdMap.get(h.getId())){
                        parent.completeHabit(h);
                        imageCheck.setImageResource(R.drawable.checked);
                        completedHabitIdMap.put(h.getId(), true);
                        Toast.makeText(parent, "Great, you earned " + h.getPoints() + " points!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(parent, "Yep, you completed this habit today!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    parent.launchEditHabit(getItem(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    public void refreshMap(){
        completedHabitIdMap = parent.getCompletedHabitMap();
    }

}
