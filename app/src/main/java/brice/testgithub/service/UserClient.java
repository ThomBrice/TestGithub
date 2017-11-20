/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.service;


import brice.testgithub.Model.AccessToken;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserClient {

    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    @GET("secretInfo")
    Call<ResponseBody> getSecret(@Header("Authotization") String authToken);
}
