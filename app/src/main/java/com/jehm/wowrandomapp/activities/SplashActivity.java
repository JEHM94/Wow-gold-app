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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jehm.wowrandomapp.API.API;
import com.jehm.wowrandomapp.API.APIServices.WoWService;
import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.models.AccessToken;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;

    private TextView textView;
    private Button button;

    private static final String GO_LOGIN = "0";
    private static final String GO_MAIN = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        button = (Button) findViewById(R.id.buttonSplash);
        textView = (TextView) findViewById(R.id.textViewSplash);

        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", "");

        if (!token.isEmpty()) {
            redirect(GO_MAIN);
        } else { //Agregar else if cuando se tenga el token del permiso
            getAccessToken(textView, SplashActivity.this);
            redirect(GO_LOGIN);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccessToken(textView, SplashActivity.this);
                redirect(GO_LOGIN);
            }
        });

    }

    private static void saveOnPreferences(String token, String token_type, int expires_in) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("token_type", token_type);
        editor.putInt("expires_in", expires_in);
        editor.commit();
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

    private void goMain() {

    }

    public static void getAccessToken(TextView textViewResponse, Context context) {

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
//                                 String tokenInfo = "Token: " + accessToken.getAccess_token() + " / " +
//                                         "Token type: " + accessToken.getToken_type() + " / "
//                                         + "Expires in: " + accessToken.getExpires_in();
//                                 textViewResponse.setText(tokenInfo);
                                     String token = accessToken.getAccess_token();
                                     String token_type = accessToken.getToken_type();
                                     int expires_in = accessToken.getExpires_in();
                                     saveOnPreferences(token, token_type, expires_in);


                                 }/* else {
                                     Toast.makeText(context, "Something bad happened :(", Toast.LENGTH_SHORT).show();
                                 }*/
                                 // Manejar el siguiente Request desde aquí
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 t.printStackTrace();
                             }
                         }
                );
    }
}