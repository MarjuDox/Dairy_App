package com.example.Dairy_App.Adapter;

import java.io.Serializable;

public class Firebasemodel implements Serializable {
    private String title;
    private String content;
    private String dateTime;
    private String isDeleted;

    public Firebasemodel(){}

    public Firebasemodel(String title , String content , String dateTime , String isDeleted) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.isDeleted = isDeleted;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsDeleted() { return isDeleted; }

    public void setIsDeleted(String isDeleted) { this.isDeleted = isDeleted;}
}

