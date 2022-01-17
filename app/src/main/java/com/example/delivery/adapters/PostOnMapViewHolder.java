package com.example.delivery.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.delivery.R;
import com.example.delivery.adapters.model.BasePostItem;
import com.example.delivery.adapters.model.PostItem;

class PostOnMapViewHolder extends PostViewHolder {

    private final TextView title;
    private final TextView listId;
    private final TextView company;
    private final TextView number;

    public PostOnMapViewHolder(@NonNull View itemView, PostAdapter.Listener listener) {
        super(itemView, listener);

        title = itemView.findViewById(R.id.item_post_title_map);
        listId = itemView.findViewById(R.id.listId_map);
        company = itemView.findViewById(R.id.item_post_company_map);
        number = itemView.findViewById(R.id.item_post_number_map);
    }

    @Override
    public void bind(BasePostItem post) {
        title.setText(post.getTitle());
        listId.setText(post.getListId());
        company.setText(post.getCompany());
        number.setText(post.getNumber());

        itemView.setOnClickListener(v -> mListener.onPostClicked(post));
    }
}