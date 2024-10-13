package com.redhat.training.camel.rest;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqContext {
    private String rdfs;
    private String onki;
    private Map<String, String> title;
    private String vocabularies;
    private String id;
    private String uri;
    @JsonProperty("@base")
    private String base;
    public String getRdfs() {
        return rdfs;
    }
    public void setRdfs(String rdfs) {
        this.rdfs = rdfs;
    }
    public String getOnki() {
        return onki;
    }
    public void setOnki(String onki) {
        this.onki = onki;
    }
    public Map<String, String> getTitle() {
        return title;
    }
    public void setTitle(Map<String, String> title) {
        this.title = title;
    }
    public String getVocabularies() {
        return vocabularies;
    }
    public void setVocabularies(String vocabularies) {
        this.vocabularies = vocabularies;
    }
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
    public String getBase() {
        return base;
    }
    public void setBase(String base) {
        this.base = base;
    }
    
}
