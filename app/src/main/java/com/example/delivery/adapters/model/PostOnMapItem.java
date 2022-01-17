package com.example.delivery.adapters.model;

import com.example.delivery.adapters.PostAdapter;
import com.example.delivery.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostOnMapItem extends BasePostItem {

    public PostOnMapItem(String documentId, String title, String listId, String company, String number, String state) {
        super(documentId, title, listId, company, number, state);
    }

    public PostOnMapItem(Post post) {
        super(post);
    }

    public static List<BasePostItem> toUiItems(List<Post> posts) {
        List<BasePostItem> postItems = new ArrayList<>();
        for (Post post : posts) {
            postItems.add(new PostOnMapItem(post));
        }
        return postItems;
    }

    @Override
    public int getViewType() {
        return PostAdapter.ITEM_LIST_ON_MAP;
    }
}
