package com.example.delivery.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delivery.R;
import com.example.delivery.adapters.model.BasePostItem;

import java.util.ArrayList;
import java.util.List;

// PostAdapter와 PostOnMapAdater는 중복으로 보임. 둘 중의 하나만 있어도 될 것 같아 보임.
public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    // 이렇게 하드코딩하는 것 보다는 용도에 따라 어댑터의 아이템을 부모 클래스가 같은 다른 자식 클래스를 사용하는 것도 좋은 접근 방법.
    // 아니면, 필드에 viewType을 가지고 가도 됨.
    public static final int ITEM_LIST = R.layout.item_list;
    public static final int ITEM_LIST_ON_MAP = R.layout.item_list_map;

    private List<BasePostItem> mPosts = new ArrayList<>();
    public Listener mListener;

    public interface Listener {
        void onPostClicked(BasePostItem post);

        void onRemovePostClicked(BasePostItem post);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void submitList(List<BasePostItem> posts) {
        this.mPosts = posts;
        notifyDataSetChanged();
    }

    public void submitList(List<BasePostItem> posts, int removedPosition) {
        this.mPosts = posts;
        notifyItemRemoved(removedPosition);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    private BasePostItem getItem(int position) {
        return mPosts.get(position);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch (viewType) {
            case ITEM_LIST:
                return new PostItemViewHolder(itemView, mListener);
            case ITEM_LIST_ON_MAP:
                return new PostOnMapViewHolder(itemView, mListener);
            default:
                throw new IllegalArgumentException("##### Cannot find view type for " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BasePostItem post = getItem(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
