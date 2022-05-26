package com.jehm.wowrandomapp.activities;


import static com.jehm.wowrandomapp.constants.Constants.ACCESS_TOKEN_URL;
import static com.jehm.wowrandomapp.constants.Constants.API_URL;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_ID;
import static com.jehm.wowrandomapp.constants.Constants.CLIENT_SECRET;
import static com.jehm.wowrandomapp.constants.Constants.DYNAMIC_NAMESPACE;
import static com.jehm.wowrandomapp.constants.Constants.LOCALE;
import static com.jehm.wowrandomapp.constants.Constants.LOGIN_CODE_URL;
import static com.jehm.wowrandomapp.constants.Constants.LOGIN_STATE;
import static com.jehm.wowrandomapp.constants.Constants.PROFILE_NAMESPACE;
import static com.jehm.wowrandomapp.constants.Constants.REDIRECT_URI;
import static com.jehm.wowrandomapp.constants.Constants.SCOPE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.jehm.wowrandomapp.API.API;
import com.jehm.wowrandomapp.API.APIServices.WoWService;
import com.jehm.wowrandomapp.R;
import com.jehm.wowrandomapp.adapters.GoldAdapter;
import com.jehm.wowrandomapp.constants.Constants;
import com.jehm.wowrandomapp.fragments.GoldFragment;
import com.jehm.wowrandomapp.models.AccessToken;
import com.jehm.wowrandomapp.models.Character;
import com.jehm.wowrandomapp.models.Utils;
import com.jehm.wowrandomapp.models.WowToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static SharedPreferences sharedPreferences;
    private TextView textViewWowToken;
    private TextView textViewPrice;
    private TextView textViewBattletag;
    private ImageView imageViewToken;
    private ImageView imageViewBattletag;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private String accessToken;
    private String authCode;
    private String authAccessToken;
    private String authAccessTokenExpiration;

    private ArrayList<Character> characterArrayList = new ArrayList<>();
    private final ArrayList<Integer> wowAccountIDs = new ArrayList<>();
    private final Map<Integer, ArrayList<Character>> subLists = new HashMap<Integer, ArrayList<Character>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.transparentStatusBar(this);
        bindUI();
        getSharedPreferences();

        getWowTokenPrice();
        renderCharacterList();
        getBattletag();


        textViewWowToken.setOnClickListener(this);
        textViewPrice.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        API.clearRetrofit();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int logout = R.id.menu_logout;
        if (item.getItemId() == logout) {
            Utils.removeSharedPreferences(sharedPreferences);
            logOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void renderCharacterList() {
        if (authAccessToken.isEmpty() || authAccessTokenExpiration.equals("Expired")) {
            getAuthAccessToken();
        }
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after xx seconds
                getCharactersInfo(MainActivity.this);
            }
        }, 1700);
    }

    private void cleanAndShowList() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after xx seconds
                progressBar.setVisibility(View.INVISIBLE);
                splitListPerID();
                ArrayList<GoldAdapter> goldAdapters = new ArrayList<>();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Predicate<Character> condition = character -> character.getMoney() < 10000;
                    for (int i = 0; i < wowAccountIDs.size(); i++) {
                        subLists.get(wowAccountIDs.get(i)).removeIf(condition);
                        if (subLists.get(wowAccountIDs.get(i)).size() != 0) {
                            goldAdapters.add(new GoldAdapter(MainActivity.this, R.layout.character_gold_layout, subLists.get(wowAccountIDs.get(i))));
                        }
                    }

                } else {
                    ArrayList<Character> tempArrayList = null;
                    for (int i = 0; i < wowAccountIDs.size(); i++) {
                        tempArrayList = new ArrayList<Character>();
                        for (int x = 0; x < subLists.get(wowAccountIDs.get(i)).size(); x++) {
                            if (subLists.get(wowAccountIDs.get(i)).get(x).getMoney() >= 10000) {
                                tempArrayList.add(subLists.get(wowAccountIDs.get(i)).get(x));
                            }
                        }
                        if (tempArrayList.size() != 0) {
                            goldAdapters.add(new GoldAdapter(MainActivity.this, R.layout.character_gold_layout, tempArrayList));
                        }
                    }
                }
                if (goldAdapters.size() > 0) {
                    GoldFragment goldFragment = (GoldFragment) getSupportFragmentManager().findFragmentById(R.id.goldFragment);
                    goldFragment.renderListFragment(MainActivity.this, goldAdapters, wowAccountIDs);
                } else {
                    Toast.makeText(MainActivity.this, "No characters found", Toast.LENGTH_LONG).show();
                }
            }
        }, 5000);
    }

    private void splitListPerID() {
        for (Character character : characterArrayList) {
            ArrayList<Character> tempList = subLists.get(character.getWowAccountID());
            if (tempList == null) {
                tempList = new ArrayList<Character>();
                subLists.put(character.getWowAccountID(), tempList);
            }
            tempList.add(character);
        }
        wowAccountIDs.addAll(subLists.keySet());
    }

    private void bindUI() {
        textViewWowToken = (TextView) findViewById(R.id.textViewWowToken);
        textViewPrice = (TextView) findViewById(R.id.wowTokenPrice);
        textViewBattletag = (TextView) findViewById(R.id.textViewBattletag);
        imageViewToken = (ImageView) findViewById(R.id.imageViewToken);
        imageViewBattletag = (ImageView) findViewById(R.id.imageViewBattletag);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void getSharedPreferences() {
        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString("accessToken", "");
        authCode = sharedPreferences.getString("authCode", "");
        authAccessToken = sharedPreferences.getString("authAccessToken", "");
        authAccessTokenExpiration = sharedPreferences.getString("auth_expires_in", "");
    }

    private static void saveOnPreferences(String authAccessToken, String auth_token_type, String auth_expires_in) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authAccessToken", authAccessToken);
        editor.putString("auth_token_type", auth_token_type);
        editor.putString("auth_expires_in", auth_expires_in);
        editor.apply();
    }

    private void getBattletag() {
        WoWService service = API.getRetrofit(ACCESS_TOKEN_URL).create(WoWService.class);
        int i = 0;
        service.getBattletag(authAccessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        ResponseBody responseBody = response.body();
                        String json = responseBody.string();
                        JSONObject mJson = new JSONObject(json);
                        String battletag = mJson.getString("battletag");
                        textViewBattletag.setText(battletag);
                        imageViewBattletag.setImageResource(Utils.getBattletagImage(battletag));
                        imageViewBattletag.setVisibility(View.VISIBLE);
                    } else {
                        System.out.println("Error trying to get user's Battletag");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

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
                                     String auth_token_type = accessToken.getToken_type();
                                     String auth_expires_in = String.valueOf(accessToken.getExpires_in());
                                     saveOnPreferences(authAccessToken, auth_token_type, auth_expires_in);
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
        WoWService service = API.getRetrofitCharacter(API_URL).create(WoWService.class);
        service.getCharacters(PROFILE_NAMESPACE, LOCALE, authAccessToken).enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if (response.body() != null) {
                    Character character = response.body();
                    characterArrayList = character.getCharacterList();
                    for (int i = 0; i < characterArrayList.size(); i++) {
                        setCharacterMoney(characterArrayList.get(i).getRealmID(), characterArrayList.get(i).getCharacterID(), i);
                    }
                    cleanAndShowList();
                } else if (response.raw().message().equals("Unauthorized")) {
                    Toast.makeText(context, "Token expired. Please Log in again.", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth_expires_in", "Expired");
                    editor.apply();
                    login();
                }
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setCharacterMoney(int realmID, int characterID, int position) {
        int rewr = 0;
        WoWService service = API.getRetrofitMoney(API_URL).create(WoWService.class);
        service.getCharacterMoney(realmID, characterID, PROFILE_NAMESPACE, LOCALE, authAccessToken).enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                if (response.body() != null) {
                    Character character = response.body();
                    characterArrayList.get(position).setMoney(character.getMoney());
                }
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getWowTokenPrice() {
        WoWService service = API.getRetrofit(API_URL).create(WoWService.class);
        service.getWowTokenPrice(DYNAMIC_NAMESPACE, LOCALE, accessToken).enqueue(new Callback<WowToken>() {
            @Override
            public void onResponse(Call<WowToken> call, Response<WowToken> response) {
                if (response.body() != null) {
                    WowToken wowToken = response.body();
                    textViewPrice.setText(formatPrice(wowToken.getPrice()));
                    imageViewToken.setVisibility(View.VISIBLE);
                    textViewPrice.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Error trying to get WoW Token price", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WowToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String formatPrice(String tokenPrice) {
        String[] priceArray = Utils.splitPrice(tokenPrice, 6);
        String[] gold = Utils.splitPrice(priceArray[0], 3);
        return gold[0] + "," + gold[1];
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

    @Override
    public void onClick(View view) {
        getWowTokenPrice();
    }
}
