package com.example.carries;

import com.example.carries.models.AccessToken;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WoWService {

    public interface GitHubService {

        // https://us.battle.net/oauth/token
        @POST("token")
        Call<AccessToken> getAccessToken(@Query("client_id") String clientId, @Query("client_secret") String clientSecret,
                                         @Query("grant_type") String grantType);
    }
}
