package com.example.identiforge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.identiforge.View.MainActivity;

public class LogoScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_screen);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                waitAndStart();
            }
        });
        thread.start();
    }

    private void waitAndStart() {
        Intent i = new Intent(this, MainActivity.class);
        try {
            Thread.sleep(2500);
            startActivity(i);
            finish();
        } catch (InterruptedException e) {
            waitAndStart();
        }
    }
}