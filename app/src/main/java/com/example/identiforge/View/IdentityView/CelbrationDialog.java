package com.example.identiforge.View.IdentityView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identiforge.Model.Helper;
import com.example.identiforge.R;

public class CelbrationDialog extends Activity {

    public static final String TITLE_EXTRA = "title";
    public static final String LEVEL_EXTRA = "level";

    private String title;
    private int level;

    private static final int PERMISSIONS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_celbration_dialog);

        Bundle b = getIntent().getExtras();
        title = b.getString(TITLE_EXTRA);
        level = b.getInt(LEVEL_EXTRA, -1);
        String message = "Congratulations!\nYou are now a level " + level + " " + title
                +".\nTake a picture to remember this moment!";

        TextView msg = findViewById(R.id.celebDialog_TextView);
        ImageView camera = findViewById(R.id.celebDialog_imageView);
        camera.setImageResource(R.drawable.camera);

        msg.setText(message);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , PERMISSIONS);
        } else {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults ){
        if(requestCode == PERMISSIONS){
            if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED){
                setResult(RESULT_OK);
                finish();
            }
        }
        else{
            setResult(RESULT_CANCELED);
            finish();        }
    }
}