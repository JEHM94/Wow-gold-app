package com.jehm.wowrandomapp.activities;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoldFragment.SortGoldListener {

    private static SharedPreferences sharedPreferences;
    private TextView textViewWowToken;
    private TextView textViewPrice;
    private TextView textViewBattletag;
    private ImageView imageViewToken;
    private ImageView imageViewBattletag;
    private ImageView imageViewSortGold;
    private ImageView imageViewSortRealm;
    private ImageView imageViewSortName;
    private ProgressBar progressBar;

    private String accessToken;
    private String authCode;
    private String authAccessToken;
    private String authAccessTokenExpiration;

    private boolean isActive;
    private boolean isAscendant = true;

    private final int goldColumn = R.string.gold;
    private final int realmColumn = R.string.realm;

    private ArrayList<Character> characterArrayList = new ArrayList<>();
    private final ArrayList<Integer> wowAccountIDs = new ArrayList<>();
    private final ArrayList<GoldAdapter> goldAdapters = new ArrayList<>();
    private final Map<Integer, ArrayList<Character>> subLists = new LinkedHashMap<>();

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

    private void bindUI() {
        textViewWowToken = findViewById(R.id.textViewWowToken);
        textViewPrice = findViewById(R.id.wowTokenPrice);
        textViewBattletag = findViewById(R.id.textViewBattletag);
        imageViewToken = findViewById(R.id.imageViewToken);
        imageViewBattletag = findViewById(R.id.imageViewBattletag);
        progressBar = findViewById(R.id.progressBar);
        Toolbar toolbar = findViewById(R.id.toolbar);

        isActive = true;
        setSupportActionBar(toolbar);
    }

    private void splitListPerID() {
        for (Character character : characterArrayList) {
            ArrayList<Character> tempList = subLists.get(character.getWowAccountID());
            if (tempList == null) {
                tempList = new ArrayList<>();
                subLists.put(character.getWowAccountID(), tempList);
            }
            tempList.add(character);
        }
        wowAccountIDs.addAll(subLists.keySet());
    }

    private void cleanList() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if (isActive) {
                //Do something after xx seconds
                progressBar.setVisibility(INVISIBLE);
                splitListPerID();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Predicate<Character> condition = character -> character.getMoney() < 10000;
                    for (int i = 0; i < wowAccountIDs.size(); i++) {
                        Objects.requireNonNull(subLists.get(wowAccountIDs.get(i))).removeIf(condition);
                        if (Objects.requireNonNull(subLists.get(wowAccountIDs.get(i))).size() != 0) {
                            goldAdapters.add(new GoldAdapter(MainActivity.this, subLists.get(wowAccountIDs.get(i))));
                        }
                    }

                } else {
                    ArrayList<Character> tempArrayList;
                    for (int i = 0; i < wowAccountIDs.size(); i++) {
                        tempArrayList = new ArrayList<>();
                        for (int x = 0; x < Objects.requireNonNull(subLists.get(wowAccountIDs.get(i))).size(); x++) {
                            if (Objects.requireNonNull(subLists.get(wowAccountIDs.get(i))).get(x).getMoney() >= 10000) {
                                tempArrayList.add(Objects.requireNonNull(subLists.get(wowAccountIDs.get(i))).get(x));
                            }
                        }
                        if (tempArrayList.size() != 0) {
                            goldAdapters.add(new GoldAdapter(MainActivity.this, tempArrayList));
                        }
                    }
                }
                showList(goldAdapters);
            }
        }, 5000);
    }

    private void showList(ArrayList<GoldAdapter> goldAdapters) {
        if (goldAdapters.size() > 0) {
            GoldFragment goldFragment = (GoldFragment) getSupportFragmentManager().findFragmentById(R.id.goldFragment);
            Objects.requireNonNull(goldFragment).renderListFragment(MainActivity.this, goldAdapters, wowAccountIDs);
        } else {
            Toast.makeText(MainActivity.this, "No characters found", Toast.LENGTH_LONG).show();
        }
    }

    private void renderCharacterList() {
        if (authAccessToken.isEmpty() || authAccessTokenExpiration.equals("Expired")) {
            getAuthAccessToken();
        }
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            //Do something after xx seconds
            getCharactersInfo(MainActivity.this);
        }, 1700);
    }

    private void getBattletag() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            //Do something after xx seconds
            WoWService service = API.getRetrofit(ACCESS_TOKEN_URL).create(WoWService.class);
            service.getBattletag(authAccessToken).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            ResponseBody responseBody = response.body();
                            String json = responseBody.string();
                            JSONObject mJson = new JSONObject(json);
                            String battletag = mJson.getString("battletag");
                            textViewBattletag.setText(battletag);
                            imageViewBattletag.setImageResource(Utils.getBattletagImage(battletag));
                            imageViewBattletag.setVisibility(VISIBLE);
                        } else {
                            System.out.println("Error trying to get user's Battletag");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }, 1000);
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
                             public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                 ResponseBody responseBody = response.body();
                                 Gson gson = new Gson();
                                 AccessToken accessToken = null;
                                 if (responseBody != null) {
                                     try {
                                         accessToken = gson.fromJson(responseBody.string(), AccessToken.class);
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                     authAccessToken = Objects.requireNonNull(accessToken).getAccess_token();
                                     String auth_token_type = accessToken.getToken_type();
                                     String auth_expires_in = String.valueOf(accessToken.getExpires_in());
                                     saveOnPreferences(authAccessToken, auth_token_type, auth_expires_in);
                                 }
                             }

                             @Override
                             public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                 t.printStackTrace();
                             }
                         }
                );
    }

    private void getCharactersInfo(Context context) {
        WoWService service = API.getRetrofitCharacter(API_URL).create(WoWService.class);
        service.getCharacters(PROFILE_NAMESPACE, LOCALE, authAccessToken).enqueue(new Callback<Character>() {
            @Override
            public void onResponse(@NonNull Call<Character> call, @NonNull Response<Character> response) {
                if (response.body() != null) {
                    Character character = response.body();
                    characterArrayList = character.getCharacterList();
                    for (int i = 0; i < characterArrayList.size(); i++) {
                        setCharacterMoney(characterArrayList.get(i).getRealmID(), characterArrayList.get(i).getCharacterID(), i);
                    }
                    cleanList();
                } else if (response.raw().message().equals("Unauthorized")) {
                    Toast.makeText(context, "Token expired. Please Log in again.", Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth_expires_in", "Expired");
                    editor.apply();
                    login();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Character> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setCharacterMoney(int realmID, int characterID, int position) {
        WoWService service = API.getRetrofitMoney(API_URL).create(WoWService.class);
        service.getCharacterMoney(realmID, characterID, PROFILE_NAMESPACE, LOCALE, authAccessToken).enqueue(new Callback<Character>() {
            @Override
            public void onResponse(@NonNull Call<Character> call, @NonNull Response<Character> response) {
                if (response.body() != null) {
                    Character character = response.body();
                    characterArrayList.get(position).setMoney(character.getMoney());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Character> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getWowTokenPrice() {
        WoWService service = API.getRetrofit(API_URL).create(WoWService.class);
        service.getWowTokenPrice(DYNAMIC_NAMESPACE, LOCALE, accessToken).enqueue(new Callback<WowToken>() {
            @Override
            public void onResponse(@NonNull Call<WowToken> call, @NonNull Response<WowToken> response) {
                if (response.body() != null) {
                    WowToken wowToken = response.body();
                    textViewPrice.setText(formatPrice(wowToken.getPrice()));
                    imageViewToken.setImageResource(R.mipmap.wowtoken_f);
                    imageViewToken.setVisibility(VISIBLE);
                    textViewPrice.setVisibility(VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "Error trying to get WoW Token price", Toast.LENGTH_LONG).show();
                    textViewPrice.setText(R.string.retry);
                    imageViewToken.setImageResource(R.drawable.ic_retry);
                    textViewPrice.setVisibility(VISIBLE);
                    imageViewToken.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WowToken> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String formatPrice(String tokenPrice) {
        String[] priceArray = Utils.splitPrice(tokenPrice, 6);
        String[] gold = Utils.splitPrice(priceArray[0], 3);
        return gold[0] + "," + gold[1];
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

    private void logOut() {
        isActive = false;
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        getSharedPreferences();
        getWowTokenPrice();
    }

    @Override
    public void sortList(int column) {
        ArrayList<GoldAdapter> newAdapters = new ArrayList<>();
        if (imageViewSortGold == null || imageViewSortRealm == null || imageViewSortName == null) {
            imageViewSortGold = findViewById(R.id.imageViewSortGold);
            imageViewSortRealm = findViewById(R.id.imageViewSortRealm);
            imageViewSortName = findViewById(R.id.imageViewSortName);
        }
        if (wowAccountIDs.size() > 0) {
            for (int i = 0; i < goldAdapters.size(); i++) {
                ArrayList<Character> characters = goldAdapters.get(i).getCharacters();
                if (characters.size() > 0) {
                    Collections.sort(characters, new Comparator<Character>() {
                        @Override
                        public int compare(Character character, Character t1) {
                            if (isAscendant) {
                                switch (column) {
                                    case goldColumn:
                                        setSortImage(true, column);
                                        return Long.compare(t1.getMoney(), character.getMoney());
                                    case realmColumn:
                                        setSortImage(true, column);
                                        return t1.getRealmName().compareTo(character.getRealmName());
                                    default:
                                        setSortImage(true, column);
                                        return t1.getCharacterName().compareTo(character.getCharacterName());
                                }
                            } else {
                                switch (column) {
                                    case goldColumn:
                                        setSortImage(false, column);
                                        return Long.compare(character.getMoney(), t1.getMoney());
                                    case realmColumn:
                                        setSortImage(false, column);
                                        return character.getRealmName().compareTo(t1.getRealmName());
                                    default:
                                        setSortImage(false, column);
                                        return character.getCharacterName().compareTo(t1.getCharacterName());
                                }
                            }
                        }

                        private void setSortImage(boolean isAscendant, int column) {
                            int imageSrc = (isAscendant) ? R.drawable.ic_baseline_arrow_drop_down_24 : R.drawable.ic_baseline_arrow_drop_up_24;
                            switch (column) {
                                case goldColumn:
                                    imageViewSortGold.setImageResource(imageSrc);
                                    imageViewSortGold.setVisibility(VISIBLE);
                                    imageViewSortRealm.setVisibility(INVISIBLE);
                                    imageViewSortName.setVisibility(INVISIBLE);
                                    break;
                                case realmColumn:
                                    imageViewSortRealm.setImageResource(imageSrc);
                                    imageViewSortRealm.setVisibility(VISIBLE);
                                    imageViewSortGold.setVisibility(INVISIBLE);
                                    imageViewSortName.setVisibility(INVISIBLE);
                                    break;
                                default:
                                    imageViewSortName.setImageResource(imageSrc);
                                    imageViewSortName.setVisibility(VISIBLE);
                                    imageViewSortGold.setVisibility(INVISIBLE);
                                    imageViewSortRealm.setVisibility(INVISIBLE);
                                    break;
                            }
                        }
                    });
                    newAdapters.add(new GoldAdapter(MainActivity.this, characters));
                }
            }
            //Simplified if(isAscendant)  isAscendant = false ; else isAscendant = true;
            isAscendant = !isAscendant;
            showList(newAdapters);
        }
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
}


