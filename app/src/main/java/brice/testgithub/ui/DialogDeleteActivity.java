/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import brice.testgithub.Model.TokenStore;
import brice.testgithub.R;
import brice.testgithub.service.GitHubClient;
import brice.testgithub.service.GithubService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DialogDeleteActivity extends Activity {

    private String repositoryName;

    private GitHubClient  client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_delete);
        client = GithubService.getGithubClient(TokenStore.getInstance(this).getToken());

        Intent intent = getIntent();
        repositoryName = intent.getStringExtra(String.valueOf(R.string.repo_name));
    }

    public void deleteClicked(View v) {
        EditText editText = (EditText) findViewById(R.id.login_user);
        String user = editText.getText().toString();

        Observable<String> observable = (client).deleteRepository(user,repositoryName);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<String>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String value) {
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t.getMessage().equals("Null is not a valid element")){
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_request),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void cancelClicked(View v){
        finish();
    }
}
