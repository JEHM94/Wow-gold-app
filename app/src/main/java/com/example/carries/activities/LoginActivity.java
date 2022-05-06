package com.example.carries.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.carries.R;

public class LoginActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
//        String token = sharedPreferences.getString("token", "");
//        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
    }
}