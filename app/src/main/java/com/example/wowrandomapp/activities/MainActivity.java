package com.example.wowrandomapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import wowrandomapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewResponse = findViewById(R.id.textViewResponse);
        //SplashActivity.getAccessToken(textViewResponse);

    }
}