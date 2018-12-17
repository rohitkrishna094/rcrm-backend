package com.rsrit.rcrm.model;

import org.bson.types.Binary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Document {

    private String title;
    private String type;
    private Binary fileAttachment;
    private String comments;

    public Document() {
    }

    public Document(String title, String type, Binary fileAttachment, String comments) {
        super();
        this.title = title;
        this.type = type;
        this.fileAttachment = fileAttachment;
        this.comments = comments;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Binary getFileAttachment() {
        return fileAttachment;
    }

    public void setFileAttachment(Binary fileAttachment) {
        this.fileAttachment = fileAttachment;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
