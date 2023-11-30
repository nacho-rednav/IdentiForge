package com.example.identiforge.Model;

import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

public class Helper {

    public static SupportSQLiteQuery getHabitDayQuery(String day){
        Log.d("message", "HELP: " + day);
        String query = "SELECT * FROM Habit WHERE " + day + " = 1;";
        return new SimpleSQLiteQuery(query);
    }

    public static String formatPoints(int points, int levelUp) {
        float progress = points/levelUp;
        String res = "";
        if(progress == 0) res = points + "------>" + levelUp;
        else if (progress < 0.4) res = "--" + points + "----->" + levelUp;
        else if(progress < 0.7) res = "----" + points + "--->" + levelUp;
        else if(progress < 0.9) res = "-----" + points + "-->" + levelUp;
        else res = "------" + points + ">" + levelUp;
        return res;
    }

    public static boolean isSafe(Object o){
        if(o == null) return false;
        if(o instanceof String && (o == "" || o == " ")) return false;
        if(o instanceof List && ((List) o).isEmpty()) return false;
        return true;
    }
}
