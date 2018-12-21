package com.rsrit.rcrm.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Document {

    @Id
    private String _id;
    private String title;
    private String type;
    private String url;
    private String comments;

    public Document() {
        this._id = ObjectId.get().toString();
    }

    public Document(String _id, String title, String type, String url, String comments) {
        super();
        this._id = _id;
        this.title = title;
        this.type = type;
        this.url = url;
        this.comments = comments;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(this);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

}
