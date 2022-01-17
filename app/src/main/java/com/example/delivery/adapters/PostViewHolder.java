package com.example.delivery.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.adapters.model.BasePostItem;
import com.example.delivery.adapters.model.PostItem;

public abstract class PostViewHolder extends RecyclerView.ViewHolder {

    protected final PostAdapter.Listener mListener;

    public PostViewHolder(@NonNull View itemView, PostAdapter.Listener listener) {
        super(itemView);
        this.mListener = listener;
    }

    abstract void bind(BasePostItem post);
}
