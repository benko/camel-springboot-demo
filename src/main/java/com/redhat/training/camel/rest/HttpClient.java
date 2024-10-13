package com.redhat.training.camel.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpConstants;
import org.springframework.stereotype.Component;

@Component
public class HttpClient extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:httpClient?repeatCount=1")
            .setHeader(HttpConstants.HTTP_QUERY, constant("q=hello+world"))
            .to("https://www.google.com/search?httpMethod=GET")
            .to("log:httpClient?showAll=true");
    }

}
