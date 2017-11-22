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

import brice.testgithub.Model.Repository;
import brice.testgithub.Model.TokenStore;
import brice.testgithub.R;
import brice.testgithub.service.GitHubClient;
import brice.testgithub.service.GithubService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRepoFragment extends Fragment {

    private Button btnTest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.creat_repo_fragment,container,false);
        btnTest = (Button) view.findViewById(R.id.btnTab2);

        GitHubClient client = GithubService.getGithubClient(TokenStore.getInstance(getContext()).getToken());

        Repository repository = new Repository();
        repository.setName("deleteRepertoire");
        final Call<Repository> createRepo = client.createNewRepo(repository);


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createRepo.enqueue(new Callback<Repository>() {

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
