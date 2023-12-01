package com.example.identiforge.View.ResultContracts;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.identiforge.Model.Habit;
import com.example.identiforge.Model.Helper;
import com.example.identiforge.View.HabitsView.CreateHabit;
import com.example.identiforge.View.IdentityView.CreateIdentity;

import java.util.ArrayList;

public class CreateHabitResultContract extends ActivityResultContract<Pair<Habit, ArrayList<String>>, Pair<Pair<Habit,String>, Boolean>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Pair<Habit, ArrayList<String>> input) {
        Intent intent = new Intent(context, CreateHabit.class);
        if(Helper.isSafe(input)){
            intent.putStringArrayListExtra(CreateHabit.LIST_EXTRA, input.second);
            if(Helper.isSafe(input.first)){
                Habit h = input.first;
                Bundle b = new Bundle();
                b.putInt(CreateHabit.ID_EXTRA, h.getId());
                b.putString(CreateHabit.TITLE_EXTRA, h.getTile());
                b.putString(CreateHabit.DESC_EXTRA, h.getDescription());
                b.putInt(CreateHabit.POINTS_EXTRA, h.getPoints());
                //Send position in the array to set it as default in the Spinner
                b.putInt(CreateHabit.IDENTITY_EXTRA, h.getIdentityId());
                b.putBoolean(CreateHabit.MON_EXTRA, h.isMonday());
                b.putBoolean(CreateHabit.TUES_EXTRA, h.isTuesday());
                b.putBoolean(CreateHabit.WEDNES_EXTRA, h.isWednesday());
                b.putBoolean(CreateHabit.THURS_EXTRA, h.isThursday());
                b.putBoolean(CreateHabit.FRI_EXTRA, h.isFriday());
                b.putBoolean(CreateHabit.SATUR_EXTRA, h.isSaturday());
                b.putBoolean(CreateHabit.SUN_EXTRA, h.isSunday());

                intent.putExtras(b);
            }
        }
        return intent;
    }

    @Override
    public Pair<Pair<Habit,String>, Boolean> parseResult(int i, @Nullable Intent intent) {
        Pair<Pair<Habit,String>, Boolean> res = null;

        if(i == RESULT_OK && intent != null){
            String title = intent.getStringExtra(CreateHabit.TITLE_EXTRA);
            String desc = intent.getStringExtra(CreateHabit.DESC_EXTRA);
            int points = intent.getIntExtra(CreateHabit.POINTS_EXTRA, 20);
            String identity = intent.getStringExtra(CreateHabit.IDENTITY_EXTRA);
            boolean mon = intent.getBooleanExtra(CreateHabit.MON_EXTRA, false);
            boolean tues = intent.getBooleanExtra(CreateHabit.TUES_EXTRA, false);
            boolean wednes = intent.getBooleanExtra(CreateHabit.WEDNES_EXTRA, false);
            boolean thurs = intent.getBooleanExtra(CreateHabit.THURS_EXTRA, false);
            boolean fri = intent.getBooleanExtra(CreateHabit.FRI_EXTRA, false);
            boolean satur = intent.getBooleanExtra(CreateHabit.SATUR_EXTRA, false);
            boolean sun = intent.getBooleanExtra(CreateHabit.SUN_EXTRA, false);
            Boolean edit = intent.getBooleanExtra(CreateHabit.EDIT_EXTRA, false);
            Habit h = new Habit(title, desc, -1, points, mon,
                    tues, wednes, thurs, fri, satur, sun);
            int id = intent.getIntExtra(CreateHabit.ID_EXTRA, -1);
            if(id != -1)
                h.setId(id);
            res = new Pair<>(new Pair<>(h, identity), edit);
        }
        return res;
    }

}
