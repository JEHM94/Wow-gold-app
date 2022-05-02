package com.example.carries;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
                .addFormDataPart("client_id", "fa75ca71e373400b9ed4c4c8c8c8c5b4")
                .addFormDataPart("client_secret", "xUEWjNdGp2ERMt6UI3UDlIvc7rwPCf7J")
                .addFormDataPart("grant_type", "client_credentials")
                .build();

        WoWService service = retrofit.create(WoWService.class);

        service.getAccessToken(requestBody).enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            ResponseBody accessToken = response.body();
                                                            accessToken.charStream();
                                                            try {
                                                                textViewResponse.setText(accessToken.string());
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

//        Call<AccessToken> accessTokenCall = service.getAccessToken(accessToken);
//
//        accessTokenCall.enqueue(new Callback<AccessToken>() {
//            @Override
//            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
//                AccessToken accessToken = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<AccessToken> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}