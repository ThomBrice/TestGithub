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

import brice.testgithub.Model.Repository;
import brice.testgithub.Model.TokenStore;
import brice.testgithub.R;
import brice.testgithub.service.GitHubClient;
import brice.testgithub.service.GithubService;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DialogEditActivity extends Activity {

    private GitHubClient  client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog_edit);
        client = GithubService.getGithubClient(TokenStore.getInstance(this).getToken());

        Intent intent = getIntent();
        String repositoryName = intent.getStringExtra(String.valueOf(R.string.repo_name));

        ((EditText) findViewById(R.id.editText_name)).setText(repositoryName);
    }

    public void editClicked(View view){
        Repository repository = new Repository();
        repository.setDescription(((EditText)findViewById(R.id.editText_description)).getText().toString());
        repository.setHomepage(((EditText)findViewById(R.id.editText_homepage)).getText().toString());
        repository.setName(((EditText)findViewById(R.id.editText_name)).getText().toString());
        String user = ((EditText)findViewById(R.id.login_user)).getText().toString();

        Observable<Repository> observable = client.editRepo(user,repository);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Repository>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Repository repository) {
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_request),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }
                });
        finish();

    }
}
