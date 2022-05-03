package com.example.carries.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carries.API.APIServices.WoWService;
import com.example.carries.R;
import com.example.carries.constants.Constants;
import com.example.carries.models.AccessToken;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewResponse = findViewById(R.id.textViewResponse);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us.battle.net/oauth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", Constants.client_id)
                .addFormDataPart("client_secret", Constants.client_secret)
                .addFormDataPart("grant_type", Constants.grant_type)
                .build();

        WoWService service = retrofit.create(WoWService.class);

        service.getAccessToken(requestBody)
                .enqueue(new Callback<ResponseBody>() {
                             @Override
                             // Siguiente try: Cambiar Response<ResponseBody> a Response<AccessToken>
                             public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                 ResponseBody responseBody = response.body();
                                 try {
                                     //textViewResponse.setText(responseBody.string());
                                     Gson gson = new Gson();
                                     AccessToken accessToken = gson.fromJson(responseBody.string(), AccessToken.class);
                                     String tokenInfo = "Token: " + accessToken.getAccess_token() + " / " +
                                             "Token type: " + accessToken.getToken_type() + " / " + "Expires in: " + accessToken.getExpires_in();
                                     textViewResponse.setText(tokenInfo);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                             }
                         }
                );
    }
}