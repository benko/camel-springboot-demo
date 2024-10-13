package com.redhat.training.camel.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http.HttpConstants;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.stereotype.Component;

@Component
public class RestClient extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().host("https://api.finto.fi:443/rest/v1")
                            .producerComponent("http");

        // Handle response codes > 299
        onException(HttpOperationFailedException.class)
            .to("direct:errorChecker")
            .handled(true);
        from("direct:errorChecker")
            .choice()
                .when(simple("${header.CamelHttpResponseCode} != 302"))
                    .throwException(new IllegalStateException("Wrong response code."))
            .end();                    

        from("timer:restClient?repeatCount=1")
            .setBody().constant("en")
            .setHeader(HttpConstants.HTTP_QUERY, simple("lang=${body}"))
            .enrich("rest:get:/vocabularies?outType=com.redhat.training.camel.rest.VocabularyList&bindingMode=json",
            (o, n) -> {
                String lang = o.getMessage().getBody(String.class);
                for (Vocabulary v : n.getMessage().getBody(VocabularyList.class).getVocabularies()) {
                    v.setLanguage(lang);
                }
                return n;
            })
            .process((e) -> {
                VocabularyList vl = e.getMessage().getBody(VocabularyList.class);
                e.getMessage().setBody(vl.getVocabularies());
            })
            .split().body()
            .to("log:restClient?showAll=true");
    }
}
