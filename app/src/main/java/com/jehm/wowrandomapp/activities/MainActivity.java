package com.jehm.wowrandomapp.activities;

import static com.jehm.wowrandomapp.constants.Constants.API_URL;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_ID;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_SECRET;
import static com.jehm.wowrandomapp.constants.Constants.DYNAMIC_NAMESPACE;
import static com.jehm.wowrandomapp.constants.Constants.GRANT_TYPE;
import static com.jehm.wowrandomapp.constants.Constants.LOCALE;
import static com.jehm.wowrandomapp.constants.Constants.PROFILE_NAMESPACE;
import static com.jehm.wowrandomapp.constants.Constants.REDIRECT_URI;
import static com.jehm.wowrandomapp.constants.Constants.SCOPE;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jehm.wowrandomapp.API.API;
import com.jehm.wowrandomapp.API.APIServices.WoWService;
import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.constants.Constants;
import com.jehm.wowrandomapp.models.Character;
import com.jehm.wowrandomapp.models.WowToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static SharedPreferences sharedPreferences;
    private TextView textViewWowToken;
    private TextView textViewPrice;
    private ImageView imageViewToken;

    private String accessToken;
    private String authCode;
    private String authAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();
        getSharedPreferences();
        getWowTokenPrice(accessToken);
        getCharactersInfo(authCode);

        textViewWowToken.setOnClickListener(this);
        textViewPrice.setOnClickListener(this);

    }

    private void bindUI() {
        textViewWowToken = (TextView) findViewById(R.id.textViewWowToken);
        textViewPrice = (TextView) findViewById(R.id.wowTokenPrice);
        imageViewToken = (ImageView) findViewById(R.id.imageViewToken);
    }

    private void getSharedPreferences() {
        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString("accessToken", "");
        authCode = sharedPreferences.getString("authCode", "");
    }

    private void getAuthAccessToken(){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", CLIENT_ID)
                .addFormDataPart("client_secret", CLIENT_SECRET)
                .addFormDataPart("redirect_uri", REDIRECT_URI)
                .addFormDataPart("scope", SCOPE)
                .addFormDataPart("grant_type", "authorization_code")
                .addFormDataPart("code", authCode)
                .build();
    }

    private void getCharactersInfo(String authCode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WoWService service = retrofit.create(WoWService.class);
        //WoWService service = API.getRetrofit(API_URL).create(WoWService.class);
        service.getCharacters(PROFILE_NAMESPACE, LOCALE, authCode).enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                Character character = response.body();

            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getWowTokenPrice(String accessToken) {
        WoWService service = API.getRetrofit(API_URL).create(WoWService.class);
        service.getWowTokenPrice(DYNAMIC_NAMESPACE, LOCALE, accessToken).enqueue(new Callback<WowToken>() {
            @Override
            public void onResponse(Call<WowToken> call, Response<WowToken> response) {
                WowToken wowToken = response.body();
                textViewPrice.setText(formatPrice(wowToken.getPrice()));
                imageViewToken.setVisibility(View.VISIBLE);
                textViewPrice.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<WowToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static String[] splitPrice(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }

    private String formatPrice(String tokenPrice) {
        String[] priceArray = splitPrice(tokenPrice, 6);
        String[] gold = splitPrice(priceArray[0], 3);
        String price = gold[0] + "," + gold[1];
        return price;
    }

    @Override
    public void onClick(View view) {
        getWowTokenPrice(accessToken);
    }
}