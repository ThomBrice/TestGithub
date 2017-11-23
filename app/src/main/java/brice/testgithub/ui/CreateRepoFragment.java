/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import brice.testgithub.Model.Repository;
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

public class CreateRepoFragment extends Fragment {

    private GitHubClient client;
    private Repository repository;
    private EditText nameEditText,descriptionEditText,homepageEditText;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.creat_repo_fragment,container,false);

        progressBar = (ProgressBar) view.findViewById(R.id.load_progress);

        nameEditText = (EditText) view.findViewById(R.id.editText_name);
        descriptionEditText = (EditText) view.findViewById(R.id.editText_description);
        homepageEditText = (EditText) view.findViewById(R.id.editText_homepage);

        client = GithubService.getGithubClient(TokenStore.getInstance(getContext()).getToken());

        ((CardView)view.findViewById(R.id.create_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClicked();
            }
        });

        return view;
    }

    public void createClicked(){
        progressBar.setVisibility(View.VISIBLE);
        if ((nameEditText.getText().toString()).equals("")){
            Toast.makeText(getContext(),getResources().getString(R.string.miss_name),Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else {
            repository = new Repository();
            repository.setName(nameEditText.getText().toString());
            repository.setHomepage(homepageEditText.getText().toString());
            repository.setDescription(descriptionEditText.getText().toString());

            Observable<Repository> observable = (client).createNewRepo(repository);

            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new Observer<Repository>(){
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(Repository repository) {
                            Toast.makeText(getContext(),getResources().getString(R.string.creation_successful),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(getContext(),getResources().getString(R.string.error_request),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onComplete() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
