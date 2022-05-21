package com.jehm.wowrandomapp.activities;

import static com.jehm.wowrandomapp.constants.Constants.CLIENT_ID;
import static com.jehm.wowrandomapp.constants.Constants.LOGIN_CODE_URL;
import static com.jehm.wowrandomapp.constants.Constants.LOGIN_STATE;
import static com.jehm.wowrandomapp.constants.Constants.REDIRECT_URI;
import static com.jehm.wowrandomapp.constants.Constants.SCOPE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.constants.Constants;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        getAuthCode();
    }

    private static void saveOnPreferences(String authCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authCode", authCode);
        editor.apply();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getAuthCode();
    }

    private void getAuthCode() {
        Intent appLinkIntent = getIntent();
        Uri appLinkData = appLinkIntent.getData();
        if (appLinkData != null) {
            String codeResponse = appLinkData.getQueryParameter("code");
            String stateResponse = appLinkData.getQueryParameter("state");
            if (!codeResponse.isEmpty() && stateResponse.equals(LOGIN_STATE)) {
                saveOnPreferences(codeResponse);
                Intent intent = new Intent(this, MainActivity.class);
                // FLAGS PARA EVITAR QUE EL USUARIO REGRESE CON EL BOTÓN ATRÁS
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View view) {
        login();
    }

    private void login() {
        String loginURL = LOGIN_CODE_URL
                + "authorize?client_id=" + CLIENT_ID
                + "&scope=" + SCOPE
                + "&state=" + LOGIN_STATE
                + "&response_type=code"
                + "&redirect_uri=" + REDIRECT_URI;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(loginURL));
        startActivity(intent);
    }
}