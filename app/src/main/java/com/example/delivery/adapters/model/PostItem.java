package com.example.delivery.adapters.model;

import com.example.delivery.adapters.PostAdapter;
import com.example.delivery.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostItem extends BasePostItem {

    public PostItem(String documentId, String title, String listId, String company, String number) {
        super(documentId, title, listId, company, number);
    }

    public PostItem(String documentId, String title, String listId, String company, String number, String state) {
        super(documentId, title, listId, company, number, state);
    }

    public PostItem(Post post) {
        super(post);
    }

    public static List<BasePostItem> toUiItems(List<Post> posts) {
        List<BasePostItem> postItems = new ArrayList<>();
        for (Post post : posts) {
            postItems.add(new PostItem(post));
        }
        return postItems;
    }

    @Override
    public int getViewType() {
        return PostAdapter.ITEM_LIST;
    }
}
