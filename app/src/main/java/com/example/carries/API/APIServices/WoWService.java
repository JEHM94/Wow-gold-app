package com.example.carries.API.APIServices;

import com.example.carries.models.AccessToken;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WoWService {
    // https://us.battle.net/oauth/token
    @POST("token")
    Call<ResponseBody> getAccessToken(@Body RequestBody body);
}
