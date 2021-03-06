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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchActivity extends AppCompatActivity {

    private String searchValue;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private TextView resultTextView;
    private ProgressBar progressBar;

    private GitHubClient client = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        resultTextView = (TextView) findViewById(R.id.result_textView);

        progressBar = ((ProgressBar) findViewById(R.id.load_progress));
        progressBar.setVisibility(View.VISIBLE);

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
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_request),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
