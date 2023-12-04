package com.example.identiforge.Model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Helper {

    public static SupportSQLiteQuery getHabitDayQuery(String day){
        String query = "SELECT * FROM Habit WHERE " + day + " = 1;";
        return new SimpleSQLiteQuery(query);
    }

    public static String formatPoints(int points, int levelUp) {
        float progress = points/levelUp;
        String res = "Points: ";
        if(progress == 0) res += points + " ------> " + levelUp;
        else if (progress < 0.4) res += "-- " + points + " -----> " + levelUp;
        else if(progress < 0.7) res += "---- " + points + " ---> " + levelUp;
        else if(progress < 0.9) res += "----- " + points + " --> " + levelUp;
        else res += "------ " + points + " > " + levelUp;
        return res;
    }

    public static boolean isSafe(Object o){
        if(o == null) return false;
        if(o instanceof String && (o == "" || o == " ")) return false;
        if(o instanceof List && ((List) o).isEmpty()) return false;
        return true;
    }

    public static Uri getUri(Context context,String title, int level) {
        Uri images;
        ContentResolver cr = context.getContentResolver();
        String name = title + "_" + level + ".jpg";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }
        else{
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, name);
        cv.put(MediaStore.Images.Media.MIME_TYPE, "images/*");

        return cr.insert(images, cv);
    }

}
