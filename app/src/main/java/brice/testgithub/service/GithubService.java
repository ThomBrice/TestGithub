/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.service;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubService {
    private static final String BASE_URL = "https://api.github.com/";
    public static final String CLIENTID = "a54e2af94831a665e4f4";
    public static final String CLIENTSECRET = "5971d0cc8639c52cb5cedc2c44f2f8682b309d2e";
    public static final String REDIRECTURI = "bricedev://callback";
    public static final String SCOPE = "repo,delete_repo,user";
    public static final String BASE_URL_TOKEN = "https://github.com/";

    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder httpClient = null;

    private static Retrofit getClient(String baseUrl, final String token){
        if (retrofit == null){
            httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder().addHeader("Authorization", "Bearer " + token);
                    return chain.proceed(newRequest.build());
                }
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static GitHubClient getGithubClient(String token){
        return getClient(BASE_URL,token).create(GitHubClient.class);
    }
}
