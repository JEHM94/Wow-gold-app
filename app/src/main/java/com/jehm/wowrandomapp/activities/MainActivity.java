package com.jehm.wowrandomapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.jehm.wowrandomapp.R;


public class MainActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String authCode = sharedPreferences.getString("authCode", "");
    }


}