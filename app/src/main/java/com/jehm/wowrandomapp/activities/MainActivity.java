package com.jehm.wowrandomapp.activities;


import static com.jehm.wowrandomapp.constants.Constants.ACCESS_TOKEN_URL;
import static com.jehm.wowrandomapp.constants.Constants.API_URL;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_ID;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_SECRET;
import static com.jehm.wowrandomapp.constants.Constants.DYNAMIC_NAMESPACE;
import static com.jehm.wowrandomapp.constants.Constants.LOCALE;
import static com.jehm.wowrandomapp.constants.Constants.PROFILE_NAMESPACE;


import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import com.jehm.wowrandomapp.API.API;
import com.jehm.wowrandomapp.API.APIServices.WoWService;
import com.jehm.wowrandomapp.R;


import com.jehm.wowrandomapp.constants.Constants;
import com.jehm.wowrandomapp.models.AccessToken;
import com.jehm.wowrandomapp.models.Character;
import com.jehm.wowrandomapp.models.WowToken;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static SharedPreferences sharedPreferences;
    private TextView textViewWowToken;
    private TextView textViewPrice;
    private ImageView imageViewToken;

    private String accessToken;
    private String authCode;
    private String authAccessToken;

    private ArrayList<Character> characterArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();
        getSharedPreferences();
        getWowTokenPrice();

        if (authAccessToken.isEmpty())
            getAuthAccessToken();
        else {
            getCharactersInfo(MainActivity.this);
        }

        //IMPORTANTE....
        //PARTIR DESDE AQUI: GENERAR EL authAccessToken Y GUARDAR EN PREFS 1 VEZ AL DÍA
        //SE ACTUALIZARÁ SOLO CON EL CÓDIGO DURANTE 1 DÍA
        //SI EL CÓDIGO EXPIRA, REDIRECCIONAR A BATTLE.NET PARA GENERAR UNO BUENO
        // getCharactersInfo(authCode);

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
        authAccessToken = sharedPreferences.getString("authAccessToken", "");
    }

    //    BORRAR
    private static void saveOnPreferences(String authAccessToken, String auth_token_type, int auth_expires_in) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authAccessToken", authAccessToken);
        editor.putString("auth_token_type", auth_token_type);
        editor.putInt("auth_expires_in", auth_expires_in);
        editor.commit();
    }
    //    BORRAR

    private void getAuthAccessToken() {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("client_id", CLIENT_ID)
                .addFormDataPart("client_secret", CLIENT_SECRET)
                .addFormDataPart("redirect_uri", Constants.REDIRECT_URI)
                .addFormDataPart("scope", Constants.SCOPE)
                .addFormDataPart("grant_type", "authorization_code")
                .addFormDataPart("code", authCode)
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
                                     authAccessToken = accessToken.getAccess_token();
//                                     BORRAR
                                     String auth_token_type = accessToken.getToken_type();
                                     int auth_expires_in = accessToken.getExpires_in();
                                     saveOnPreferences(authAccessToken, auth_token_type, auth_expires_in);
//                                     BORRAR
                                 }
                             }

                             @Override
                             public void onFailure(Call<ResponseBody> call, Throwable t) {
                                 t.printStackTrace();
                             }
                         }
                );
    }

    private void getCharactersInfo(Context context) {
        int yi = 0;
        WoWService service = API.getRetrofitCharacter(API_URL).create(WoWService.class);
        service.getCharacters(PROFILE_NAMESPACE, LOCALE, "authAccessToken").enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if (response.body() != null) {
                    Character character = response.body();
                    characterArrayList = character.getCharacterList();
                    for (int i = 0; i < characterArrayList.size(); i++) {
                        setCharacterMoney(characterArrayList.get(i).getRealmID(), characterArrayList.get(i).getCharacterID(), i);
                    }
                } else if (response.raw().message().equals("Unauthorized")) {
                    Toast.makeText(context, "Token expired", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setCharacterMoney(int realmID, int characterID, int position) {
        int i = 0;
        WoWService service = API.getRetrofitMoney(API_URL).create(WoWService.class);
        service.getCharacterMoney(realmID, characterID, PROFILE_NAMESPACE, LOCALE, authAccessToken).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    Integer money = response.body();
                    characterArrayList.get(position).setMoney(money);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void cleanList() {

    }

    private void getWowTokenPrice() {
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
        return gold[0] + "," + gold[1];
    }

    @Override
    public void onClick(View view) {
        getWowTokenPrice();
    }
}