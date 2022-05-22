package com.jehm.wowrandomapp.activities;

import static com.jehm.wowrandomapp.constants.Constants.ACCESS_TOKEN_URL;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_ID;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_SECRET;
import static com.jehm.wowrandomapp.constants.Constants.GRANT_TYPE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;

import com.jehm.wowrandomapp.API.API;
import com.jehm.wowrandomapp.API.APIServices.WoWService;
import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.models.AccessToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;
    private String token;
    private String authCode;
    private String tokenExpirationDate;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

    private static final String GO_LOGIN = "0";
    private static final String GO_MAIN = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSharedPreferences();
        verifyTokenAndRedirect();
        SystemClock.sleep(3000);
        //Destruye la instancia del activity para no volver
        finish();
    }

    private void verifyTokenAndRedirect() {
        if (!token.isEmpty() && !authCode.isEmpty()) {
            if(isTokenExpired(tokenExpirationDate)){
                getAccessToken();
            }
            redirect(GO_MAIN);
        } else if (!token.isEmpty()) {
            if(isTokenExpired(tokenExpirationDate)){
                getAccessToken();
            }
            redirect(GO_LOGIN);
        } else {
            getAccessToken();
            redirect(GO_LOGIN);
        }
    }

    private static String setTokenExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date expirationDate = calendar.getTime();

        return dateFormat.format(expirationDate);
    }

    private boolean isTokenExpired(String tokenExpirationDate) {
        try {
            Date expirationDate = dateFormat.parse(tokenExpirationDate);
            int result = Calendar.getInstance().getTime().compareTo(expirationDate);
            return result >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void getSharedPreferences() {
        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("accessToken", "");
        authCode = sharedPreferences.getString("authCode", "");
        tokenExpirationDate = sharedPreferences.getString("token_date", "");
    }

    private static void saveOnPreferences(String token, String token_type, int expires_in, String token_date) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessToken", token);
        editor.putString("token_type", token_type);
        editor.putInt("expires_in", expires_in);
        editor.putString("token_date", token_date);
        editor.apply();
    }

    private void redirect(String goTo) {
        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentMain = new Intent(this, MainActivity.class);
        switch (goTo) {
            case GO_LOGIN:
                startActivity(intentLogin);
                break;
            case GO_MAIN:
                startActivity(intentMain);
                break;
            default:
                break;
        }
    }

    public static void getAccessToken() {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", CLIENT_ID)
                .addFormDataPart("client_secret", CLIENT_SECRET)
                .addFormDataPart("grant_type", GRANT_TYPE)
                .build();

        WoWService service = API.getRetrofit(ACCESS_TOKEN_URL).create(WoWService.class);

        service.getAccessToken(requestBody)
                .enqueue(new Callback<ResponseBody>() {
                             @Override
                             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                 ResponseBody responseBody = response.body();
                                 Gson gson = new Gson();
                                 AccessToken accessToken = null;
                                 if (responseBody != null) {
                                     try {
                                         accessToken = gson.fromJson(responseBody.string(), AccessToken.class);
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                     String token = accessToken.getAccess_token();
                                     String token_type = accessToken.getToken_type();
                                     int expires_in = accessToken.getExpires_in();
                                     String expirationDate = setTokenExpirationDate();
                                     saveOnPreferences(token, token_type, expires_in, expirationDate);
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 t.printStackTrace();
                             }
                         }
                );
    }
}