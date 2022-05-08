package com.example.wowrandomapp.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static Retrofit retrofit = null;
    private static String lastURL= null;

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
}
