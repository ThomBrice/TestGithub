/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import brice.testgithub.Model.Repository;
import brice.testgithub.Model.TokenStore;
import brice.testgithub.R;
import brice.testgithub.service.GitHubClient;
import brice.testgithub.service.GithubService;
import brice.testgithub.utils.RepositoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepotFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private GitHubClient  client = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.depot_fragment,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        client = GithubService.createService(GitHubClient.class ,TokenStore.getInstance(getContext()).getToken());

        getListRepositories();

        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = ((RepositoryAdapter)adapter).getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (client != null){
                    (client.deleteRepository("ThomBrice", "testDelete")).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            response.body();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.action_rename:
                Toast.makeText(getContext(),R.string.action_rename,Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getListRepositories();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void getListRepositories(){

        Call<List<Repository>> call = client.reposForUser("ThomBrice");

        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                List<Repository> repos = response.body();

                adapter = new RepositoryAdapter(repos);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                Toast.makeText(getActivity(),"error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void handleResponse(List<Repository> repositories){
        adapter = new RepositoryAdapter(repositories);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void handleError(Throwable error){
        Toast.makeText(getContext(),"Error", Toast.LENGTH_SHORT).show();
    }
}
