package com.example.delivery.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.delivery.R;
import com.example.delivery.adapters.model.BasePostItem;
import com.example.delivery.adapters.model.PostItem;
import com.example.delivery.models.Post;

class PostItemViewHolder extends PostViewHolder {
    private final TextView title;
    private final TextView listId;
    private final TextView company;
    private final TextView number;
    private final TextView deleteBtn;
    // RecyclerView.Adapter와 RecyclerView.ViewHolder의 역할은 주어진 데이터를 가지고
    // 화면에 데이터를 어떻게 보여주는가이지, 데이터를 어떻게 가져와야하는지가 아니므로,
    // state에 필요한 데이터는 외부에서 어댑터에 필요한 데이터를 다 가져오고 나서 어댑터에 데이터를 제공하도록 수정.
    private final TextView state;

    public PostItemViewHolder(@NonNull View itemView, PostAdapter.Listener listener) {
        super(itemView, listener);

        title = itemView.findViewById(R.id.item_post_title);
        listId = itemView.findViewById(R.id.listId);
        company = itemView.findViewById(R.id.item_post_company);
        number = itemView.findViewById(R.id.item_post_number);
        state = itemView.findViewById(R.id.state);
        deleteBtn = itemView.findViewById(R.id.btn_x);
    }

    @Override
    public void bind(BasePostItem post) {
        title.setText(post.getTitle());
        listId.setText(post.getListId());
        company.setText(post.getCompany());
        number.setText(post.getNumber());

        deleteBtn.setOnClickListener(v -> mListener.onRemovePostClicked(post));
        itemView.setOnClickListener(v -> mListener.onPostClicked(post));
    }
}