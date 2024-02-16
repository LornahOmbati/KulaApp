package com.example.kulaapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    Activity act;
    Context ctx;

    private static final int  SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        act = this;
        ctx = this;


        // splash screen code to navigate automatically to main activity after a delay/timeout of 3seconds
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, ExploreActivity
                    .class);
            startActivity(mainIntent);

            finish();
        },SPLASH_TIME_OUT);
    }
}