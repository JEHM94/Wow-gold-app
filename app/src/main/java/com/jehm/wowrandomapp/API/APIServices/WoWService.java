package com.jehm.wowrandomapp.API.APIServices;

import com.jehm.wowrandomapp.models.AuthorizeCode;
import com.jehm.wowrandomapp.models.Character;
import com.jehm.wowrandomapp.models.WowToken;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WoWService {
    // https://us.battle.net/oauth/token
    @POST("token")
    Call<ResponseBody> getAccessToken(@Body RequestBody body);

    //https://us.api.blizzard.com/data/wow/token/
    // index?namespace=dynamic-us&locale=en_US&access_token=USU3vnnBeHYReiBefPweK0dBbox8LK8qDR
    @GET("data/wow/token/index")
    Call<WowToken> getWowTokenPrice(@Query("namespace") String namespace,
                                    @Query("locale") String locale,
                                    @Query("access_token") String access_token);

    //https://us.api.blizzard.com/profile/user/wow
    @GET("profile/user/wow")
    Call<Character> getCharacters(@Query("namespace") String namespace,
                                  @Query("locale") String locale,
                                  @Query("access_token") String access_token);


    //https://us.api.blizzard.com/profile/user/wow/protected-character/{realmId}-{characterId}
    @GET("profile/user/wow/protected-character/{realmId}-{characterId}")
    Call<Integer> getCharacterMoney(@Path("realmId") int realmId,
                                    @Path("characterId") int characterId,
                                    @Query("namespace") String namespace,
                                    @Query("locale") String locale,
                                    @Query("access_token") String access_token);
}
