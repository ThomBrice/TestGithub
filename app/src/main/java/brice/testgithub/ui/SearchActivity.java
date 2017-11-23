/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import brice.testgithub.Model.Repository;
import brice.testgithub.Model.SearchAnswer;
import brice.testgithub.Model.TokenStore;
import brice.testgithub.R;
import brice.testgithub.service.GitHubClient;
import brice.testgithub.service.GithubService;
import brice.testgithub.utils.RepositoryAdapter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private String searchValue;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView resultTextView;

    private GitHubClient client = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        resultTextView = (TextView) findViewById(R.id.result_textView);

        client = GithubService.getGithubClient(TokenStore.getInstance(getApplicationContext()).getToken());

        Intent intent = getIntent();
        searchValue = intent.getStringExtra(getResources().getString(R.string.search_value));

        getListRepositories();
    }

    private void getListRepositories(){

        Observable<SearchAnswer> observable = client.searchRepos(searchValue);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<SearchAnswer>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("errr","onSubscribe");
                    }

                    @Override
                    public void onNext(SearchAnswer answer) {
                        resultTextView.setText(String.valueOf(answer.getTotalCount()));
                        adapter = new RepositoryAdapter(answer.getRepositories());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Log.e("errr","onNext");
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("errr","onError" + t);

                    }

                    @Override
                    public void onComplete() {
                        Log.e("errr","onComplete");
                    }
                });
    }
}
