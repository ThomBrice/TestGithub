/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import brice.testgithub.Model.AccessToken;
import brice.testgithub.Model.Repository;
import brice.testgithub.Model.TokenStore;
import brice.testgithub.R;
import brice.testgithub.service.GitHubClient;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateRepoFragment extends Fragment {

    private Button btnTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2_fragment,container,false);
        btnTest = (Button) view.findViewById(R.id.btnTab2);

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder().addHeader("Authorization", "Bearer " + TokenStore.getInstance(getContext()).getToken());
                return chain.proceed(newRequest.build());
            }
        });

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .baseUrl(GitHubClient.endPoint)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        final GitHubClient client = retrofit.create(GitHubClient.class);


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Repository repository = new Repository("creationTest");
                client.createNewRepo(repository).enqueue(new Callback<Repository>() {

                    @Override
                    public void onResponse(Call<Repository> call, Response<Repository> response) {
                        response.body();
                    }

                    @Override
                    public void onFailure(Call<Repository> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }
}
