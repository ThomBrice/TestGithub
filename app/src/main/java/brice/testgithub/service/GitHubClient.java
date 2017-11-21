/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.service;


import java.util.List;

import brice.testgithub.Model.AccessToken;
import brice.testgithub.Model.Repository;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubClient {

    String clientId = "a54e2af94831a665e4f4";
    String clientSecret = "5971d0cc8639c52cb5cedc2c44f2f8682b309d2e";
    String redirectUri = "bricedev://callback";
    String scope = "repo,delete_repo,user";
    String endPoint = "https://api.github.com/";

    @Headers("Accept: application/json") //retrofit2 will specify to github that we want json datas
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    @GET("/users/{user}/repos")
    Call<List<Repository>> reposForUser(@Path("user") String user);

    @POST("/search/repositories")
    Call<List<Repository>> searchRepos(@Field("q") String query);

    @POST("/user/repos")
    Call<Repository> createNewRepo(
            @Body Repository repository
    );

}
