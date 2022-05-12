package com.jehm.wowrandomapp.activities;

import static com.jehm.wowrandomapp.constants.Constants.ACCESS_TOKEN_URL;
import static com.jehm.wowrandomapp.constants.Constants.API_URL;
import static com.jehm.wowrandomapp.constants.Constants.DYNAMIC_NAMESPACE;
import static com.jehm.wowrandomapp.constants.Constants.LOCALE;
import static com.jehm.wowrandomapp.constants.Constants.WOW_TOKEN_API_URL;

import androidx.appcompat.app.AppCompatActivity;
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
import com.jehm.wowrandomapp.models.AccessToken;
import com.jehm.wowrandomapp.models.WowToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();
        getSharedPreferences();
        getWowTokenPrice(accessToken);

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