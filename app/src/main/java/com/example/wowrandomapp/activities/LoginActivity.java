package com.example.wowrandomapp.activities;

import static com.example.wowrandomapp.constants.Constants.CLIENT_ID;
import static com.example.wowrandomapp.constants.Constants.LOGIN_CODE_URL;
import static com.example.wowrandomapp.constants.Constants.LOGIN_STATE;
import static com.example.wowrandomapp.constants.Constants.REDIRECT_URI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import wowrandomapp.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin;
    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
//        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
//        String token = sharedPreferences.getString("token", "");
//        Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
        //getAuthCode();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getAuthCode();
    }

    private void getAuthCode() {
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        if(appLinkData != null ){
            buttonLogin.setText(appLinkAction);
        }
    }

    @Override
    public void onClick(View view) {
        login();
    }

    private void login() {
        String loginURL = LOGIN_CODE_URL
                + "authorize?client_id=" + CLIENT_ID
                + "&scope=wow.profile"
                + "&state=" + LOGIN_STATE
                + "&response_type=code"
                + "&redirect_uri="+ REDIRECT_URI;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(loginURL));
        startActivity(intent);
    }

    private void getAuthorizeCode() {

    }
}