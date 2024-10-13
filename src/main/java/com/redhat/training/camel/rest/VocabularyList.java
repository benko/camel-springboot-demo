package com.redhat.training.camel.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VocabularyList {
    @JsonProperty("@context")
    private ReqContext context;
    private String uri;
    private List<Vocabulary> vocabularies;
    public ReqContext getContext() {
        return context;
    }
    public void setContext(ReqContext context) {
        this.context = context;
    }
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public List<Vocabulary> getVocabularies() {
        return vocabularies;
    }
    public void setVocabularies(List<Vocabulary> vocabularies) {
        this.vocabularies = vocabularies;
    }
    
}
