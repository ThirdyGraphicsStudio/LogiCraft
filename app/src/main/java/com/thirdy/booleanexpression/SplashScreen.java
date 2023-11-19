package com.thirdy.booleanexpression;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
// Set a delay of 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start MainActivity after 2 seconds
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the SplashActivity
            }
        }, 2000); // 2000 milliseconds = 2 second
    }
}