package com.example.delivery.models;

public class Post {

    private String documentId;
    private String title;
    private String listId;
    private String company;
    private String number;

    public Post() {
    }

    public Post(String documentId, String title, String listId, String company, String number) {
        this.documentId = documentId;
        this.title = title;
        this.listId = listId;
        this.company = company;
        this.number = number;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
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

    @Override
    public String toString() {
        return "Post{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", listId='" + listId + '\'' +
                ", company='" + company + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
