/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import brice.testgithub.Model.AccessToken;
import brice.testgithub.Model.TokenStore;
import brice.testgithub.service.GitHubClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private final String clientId = "a54e2af94831a665e4f4";
    private final String clientSecret = "5971d0cc8639c52cb5cedc2c44f2f8682b309d2e";
    private final String redirectUri = "bricedev://callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith(redirectUri)){
            String code = uri.getQueryParameter("code");

            if (code != null){
                //get the token
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("https://github.com/")
                        .addConverterFactory(GsonConverterFactory.create()); //because response is in json and we want to create a java object

                Retrofit retrofit= builder.build();

                GitHubClient client = retrofit.create(GitHubClient.class);
                Call<AccessToken> accessTokenCall = client.getAccessToken(
                        clientId,
                        clientSecret,
                        code
                );

                accessTokenCall.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        Toast.makeText(LoginActivity.this, "yes", Toast.LENGTH_SHORT).show();
                        TokenStore.getInstance(LoginActivity.this).saveToken(response.body().getAccessToken()); // the token is stored to avoid the user to register everytime
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "no", Toast.LENGTH_SHORT).show();
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
                Uri.parse("https://github.com/login/oauth/authorize" +
                        "?client_id=" + clientId +
                        "&scope=repo,delete_repo" + // because we only need to have the information of repositories
                        "&redirect_uri=" + redirectUri));
        startActivity(intent);
    }
}
