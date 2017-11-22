/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.service;


import java.util.List;

import brice.testgithub.Model.AccessToken;
import brice.testgithub.Model.Contributor;
import brice.testgithub.Model.Repository;
import brice.testgithub.Model.User;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubClient {

    @Headers("Accept: application/json") //retrofit2 will specify to github that we want json datas
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(  //get the Token
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    @GET("/user")
    Call<User> getUserInfo();

    @GET("/user/repos")   // get all user's repos (thanks to auhtorization token)
    Observable<List<Repository>> getUserRepos();

    @GET("/search/repositories")  //search for repo(s)
    Call<List<Repository>> searchRepos(
            @Query("q") String query
    );

    @POST("/user/repos")  // create a new repo
    Observable<Repository> createNewRepo(
            @Body Repository repository
    );

    @DELETE("repos/{user}/{repo}") //delete a repo
    Observable<String> deleteRepository(
            @Path("user") String user,
            @Path("repo") String repo
    );

    @GET("/repos/{owner}/{repo}/contributors") // get contributors of a repo
    Call<List<Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);
}
