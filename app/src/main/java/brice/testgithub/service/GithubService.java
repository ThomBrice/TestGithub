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

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass, final String token){
        if (token != null){
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request.Builder newRequest = request.newBuilder().addHeader("Authorization", "Bearer " + token);
                    return chain.proceed(newRequest.build());
                }
            });
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }
}
