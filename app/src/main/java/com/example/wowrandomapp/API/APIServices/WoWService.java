package com.example.wowrandomapp.API.APIServices;

import com.example.wowrandomapp.models.AuthorizeCode;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WoWService {
    // https://us.battle.net/oauth/token
    @POST("token")
    Call<ResponseBody> getAccessToken(@Body RequestBody body);

    @GET("authorize")
    Call<AuthorizeCode> getAuthorizeCode(@Query("client_id") String client_id,
                                         @Query("scope") String scope,
                                         @Query("state") String state,
                                         @Query("response_type") String response_type,
                                         @Query("redirect_uri") String redirect_uri);
}
