package com.jehm.wowrandomapp.API;

import com.google.gson.GsonBuilder;
import com.jehm.wowrandomapp.deserializers.CharactersDeserializer;
import com.jehm.wowrandomapp.deserializers.MoneyDeserializer;
import com.jehm.wowrandomapp.models.Character;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static Retrofit retrofit = null;
    private static String lastURL = "";
    private static String lastCharacterURL = "";
    private static String lastMoneyURL = "";

    public static Retrofit getRetrofit(String requestURL) {
        if (retrofit == null || !lastURL.equals(requestURL)) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(requestURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            lastURL = requestURL;
        }
        return retrofit;
    }

    public static Retrofit getRetrofitCharacter(String requestURL) {
        if (retrofit == null || !lastCharacterURL.equals(requestURL)) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Character.class, new CharactersDeserializer());

            retrofit = new Retrofit.Builder()
                    .baseUrl(requestURL)
                    .addConverterFactory(GsonConverterFactory.create(builder.create()))
                    .build();
            lastCharacterURL = requestURL;
        }
        return retrofit;
    }

    public static Retrofit getRetrofitMoney(String requestURL) {
        if (retrofit == null || !lastMoneyURL.equals(requestURL)) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Integer.class, new MoneyDeserializer());

            retrofit = new Retrofit.Builder()
                    .baseUrl(requestURL)
                    .addConverterFactory(GsonConverterFactory.create(builder.create()))
                    .build();
            lastMoneyURL = requestURL;
        }
        return retrofit;
    }

    public static void clearRetrofit() {
        retrofit = null;
        lastURL = "";
        lastCharacterURL = "";
        lastMoneyURL = "";
    }
}