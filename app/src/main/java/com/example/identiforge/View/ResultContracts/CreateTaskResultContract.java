package com.example.identiforge.View.ResultContracts;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.identiforge.Model.DateHelper;
import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.Task;
import com.example.identiforge.View.TasksView.CreateTask;

import java.util.ArrayList;

public class CreateTaskResultContract extends ActivityResultContract<Pair<Task, ArrayList<String>>, Pair<Pair<Task,String>, Boolean>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Pair<Task, ArrayList<String>> input) {
        Intent intent = new Intent(context, CreateTask.class);
        if(Helper.isSafe(input)){
            intent.putStringArrayListExtra(CreateTask.LIST_EXTRA, input.second);
            if(Helper.isSafe(input.first)){
                Task t = input.first;
                Bundle b = new Bundle();
                b.putInt(CreateTask.ID_EXTRA, t.getId());
                b.putString(CreateTask.TITLE_EXTRA, t.getTitle());
                b.putString(CreateTask.DESC_EXTRA, t.getDescription());
                b.putInt(CreateTask.POINTS_EXTRA, t.getPoints());
                //Send position in the array to set it as default in Spinner
                b.putInt(CreateTask.IDENTITY_EXTRA, t.getIdentityId());
                b.putString(CreateTask.DAY_EXTRA, t.getDay());

                intent.putExtras(b);
            }
        }

        return intent;
    }

    @Override
    public Pair<Pair<Task, String>, Boolean> parseResult(int i, @Nullable Intent intent) {
        Pair<Pair<Task, String>, Boolean> res = null;

        if(i == RESULT_OK && intent != null){
            String title = intent.getStringExtra(CreateTask.TITLE_EXTRA);
            String desc = intent.getStringExtra(CreateTask.DESC_EXTRA);
            int points = intent.getIntExtra(CreateTask.POINTS_EXTRA, 20);
            String identity = intent.getStringExtra(CreateTask.IDENTITY_EXTRA);
            String day = intent.getStringExtra(CreateTask.DAY_EXTRA);
            Boolean edit = intent.getBooleanExtra(CreateTask.EDIT_EXTRA, false);
            long tmstamp = DateHelper.getTimeStamp(day);
            Task t = new Task(title, desc, -1, points, tmstamp);
            int id = intent.getIntExtra(CreateTask.ID_EXTRA, -1);
            if(id != -1) t.setId(id);

            res = new Pair<>(new Pair<>(t, identity), edit);
        }

        return res;
    }
}
