/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import brice.testgithub.Model.AccessToken;
import brice.testgithub.Model.TokenStore;
import brice.testgithub.R;
import brice.testgithub.service.GitHubClient;
import brice.testgithub.service.GithubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TokenStore tokenStore = TokenStore.getInstance(getApplicationContext());
        if (tokenStore.getToken() != null){
            openMain();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith(GithubService.REDIRECTURI)){
            String code = uri.getQueryParameter("code");

            if (code != null){
                //get the token
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(GithubService.BASE_URL_TOKEN)
                        .addConverterFactory(GsonConverterFactory.create()); //because response is in json and we want to create a java object

                Retrofit retrofit= builder.build();

                GitHubClient client = retrofit.create(GitHubClient.class);
                
                Call<AccessToken> accessTokenCall = client.getAccessToken(
                        GithubService.CLIENTID,
                        GithubService.CLIENTSECRET,
                        code
                );

                accessTokenCall.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        TokenStore.getInstance(LoginActivity.this).saveToken(response.body().getAccessToken()); // the token is stored to avoid the user to register everytime
                        openMain();
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {

                    }
                });
            } else if (uri.getQueryParameter("error") != null){
                //TODO error message
            }
        }
    }

    public void logInClicked(View view){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com" +
                        "/login/oauth/authorize" +
                        "?client_id=" + GithubService.CLIENTID +
                        "&scope=" + GithubService.SCOPE+
                        "&redirect_uri=" + GithubService.REDIRECTURI));
        startActivity(intent);
    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
