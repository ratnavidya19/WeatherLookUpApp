package com.ratnavidyakanawade.myapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ratnavidyakanawade.myapplication.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // This method is used so that splash activity can cover the entire screen.
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent is used to switch from one activity to another.
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i); // invoke the MainActivity.
                finish(); // the current activity will get finished.
            }
        }, 3000);
    }
}