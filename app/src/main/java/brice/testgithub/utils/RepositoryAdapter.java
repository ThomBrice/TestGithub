/*
 * Created by Thomas Brice
 * Copyright (c) 2017.
 */

package brice.testgithub.utils;


import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import brice.testgithub.Model.Repository;
import brice.testgithub.R;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder>{

    List<Repository> list;
    private int position;

    public RepositoryAdapter(List<Repository> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repository,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder myViewHolder, int position) {
        Repository repository = list.get(position);
        myViewHolder.bind(repository);
        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(myViewHolder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getPosition() {
        return position;
    }

    public Repository getItem(){
        return list.get(position);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private TextView name_textView;
        private TextView condidentiality_textView;
        private TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            name_textView = (TextView) itemView.findViewById(R.id.repo_full_name);
            condidentiality_textView = (TextView) itemView.findViewById(R.id.confidential_state);
            description = (TextView) itemView.findViewById(R.id.description);
            itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);
        }

        public void bind(Repository repository){
            name_textView.setText(repository.getFullName());
            condidentiality_textView.setText(String.valueOf(repository.getPrivate()));
            description.setText(repository.getDescription());
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(Menu.NONE, R.id.action_rename, Menu.NONE, R.string.action_rename);//groupId, itemId, order, title
            contextMenu.add(Menu.NONE, R.id.action_delete, Menu.NONE, R.string.action_delete);
        }
    }
}
