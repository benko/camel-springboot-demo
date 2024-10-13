package com.redhat.training.camel.rest;

public class Vocabulary {
    private String id;
    private String uri;
    private String title;
    private String language;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String toString() {
        return "Vocabulary[id=" + id + ",uri=" + uri + ",lang=" + language + ",title=" + title + "]";
    }
}
