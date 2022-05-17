package com.jehm.wowrandomapp.API;

import com.google.gson.GsonBuilder;
import com.jehm.wowrandomapp.deserializers.CharactersDeserializer;
import com.jehm.wowrandomapp.models.Character;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static Retrofit retrofit = null;
    private static String lastURL = "";
    private static String lastCharacterURL = "";

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
}