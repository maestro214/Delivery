package com.example.delivery.adapters.model;

import com.example.delivery.adapters.PostAdapter;
import com.example.delivery.models.Post;

public class BasePostItem {
    private String documentId;
    private String title;
    private String listId;
    private String company;
    private String number;
    private String state;

    public BasePostItem(String documentId, String title, String listId, String company, String number) {
        this.documentId = documentId;
        this.title = title;
        this.listId = listId;
        this.company = company;
        this.number = number;
    }

    public BasePostItem(String documentId, String title, String listId, String company, String number, String state) {
        this.documentId = documentId;
        this.title = title;
        this.listId = listId;
        this.company = company;
        this.number = number;
        this.state = state;
    }

    public BasePostItem(Post post) {
        this(
            post.getDocumentId(),
            post.getTitle(),
            post.getListId(),
            post.getCompany(),
            post.getNumber()
        );
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getViewType() {
        return 0;
    }
}
