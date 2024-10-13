package com.redhat.training.camel.bookstore;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.springframework.stereotype.Component;

import com.redhat.training.camel.bookstore.model.FulfillmentOrder;
import com.redhat.training.camel.bookstore.model.Order;
import com.redhat.training.camel.bookstore.model.User;
import com.redhat.training.camel.bookstore.registry.BookNotFoundException;
import com.redhat.training.camel.bookstore.registry.UserNotFoundException;

@Component
public class BookstoreRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // one of the builtin error handlers, global to the class and all exceptions
        // (unless more specific rules apply by means of onException(), or in the route)
        // errorHandler(defaultErrorHandler());
        // errorHandler(noErrorHandler());
        // errorHandler(deadLetterChannel("file:data/errors/?fileName=error.txt&fileExist=Append"));

        // exception-specific rule(s)
        // onException(IllegalArgumentException.class)
        //     .to("file:error/?fileName=iae.log&fileExist=Append")
        //     .handled(true)
        //     .maximumRedeliveries(3);

        // read the book catalog and add it to a singleton list
        from("file:data/bookstore/?fileName=books.xml&noop=true")
            .routeId("bookLoader")
            .to("log:bookInitialRead?showAll=true")
            // split on an xpath expression
            .split().xpath("/bookstore/book") 
            .to("log:booksAfterSplit?showAll=true")
            // pay attention to src/main/resources/com/redhat/training/camel/bookstore/model/jaxb.index
            .unmarshal(new JaxbDataFormat("com.redhat.training.camel.bookstore.model"))
            .to("log:bookLogger?showAll=true")
            // use simple expression language to only sell books newer than 2003
            .filter(simple("${body.year} > 2003"))
            // use a custom predicate (we can't sell G.R.R.Martin)
            .filter(new MyBookFilterPredicate())
            .to("log:booksAfterFilter?showAll=true")
            // add the book to the catalog, if still here
            .to("bean:bookCatalog?method=simpleAdd");

        // XML2JSON conversion
        // .to("xj:identity?transformDirection=XML2JSON")
        // .to("log:afterTransform");

        // load the book catalog and add it to a singleton list
        from("file:data/bookstore/?fileName={{users.file:users.csv}}&noop=true")
            .routeId("userLoader")
            .to("log:usersInitialRead?showAll=true")
            // UNSAFE: a malformed line in the users.csv file will kill the entire file read.
            // .doTry()
            //     .unmarshal(new BindyCsvDataFormat(User.class))
            // .doCatch(IllegalArgumentException.class)
            //     .to("log:errorLog?showAll=true")
            // .end()
            // .to("log:usersAfterUnmarshal?showAll=true")
            // INSTEAD: split first, ignore the header line, and unmarshal each line separately.
            .split().tokenize("\n").streaming()
            .choice()
                // recognise the header line and stop processing that exchange
                .when().simple("${body} == 'Identifier;Access code;Recovery code;First name;Last name;Department;Location'")
                    .log("Skipping CSV header line")
                    .stop()
                .end()
            .to("log:userLogger?showAll=true")
            // attempt to unmarshal CSV to User
            // ignore IllegalArgumentExceptions as they signify a data format error
            .doTry()
                .unmarshal(new BindyCsvDataFormat(User.class))
            .doCatch(IllegalArgumentException.class)
                .log("Ignoring parsing error: " + exceptionMessage())
                .stop()
            .end()
            .to("log:userPostUnmarshal?showAll=true")
            // spin off to aggregate users by team
            .wireTap("direct:aggregateByTeam")
            // this will handle duplicate user exceptions
            .doTry()
                .to("bean:userCatalog")
            .doCatch(IllegalArgumentException.class)
                .log("Ignoring IllegalArgumentException: " + exceptionMessage())
            .end();

        // aggregate users by team
        from("direct:aggregateByTeam")
            .routeId("userAggregator")
            // key is user.department
            .aggregate(simple("${body.department}"),
                        new TeamAggregator())
                .completionSize(10)
                .completionTimeout(1500)
            .to("log:logTeam?showAll=true")
            // marshal everything to JSON once the group completes
            .marshal(new JacksonDataFormat())
            .to("log:afterMarshal")
            // write it to teams.json
            .to("file:data/out/?fileName=teams.json&fileExist=Append");

        // load the orders from the JSON file
        from("file:data/bookstore/?fileName=orders.json&noop=true")
            .routeId("orderLoader")
            .to("log:ordersInitialRead?showAll=true")
            // Jackson will produce a list of Order objects that we can split on
            .unmarshal(new ListJacksonDataFormat(Order.class))
            .to("log:ordersBeforeSplit?showAll=true")
            .split().body()
            // pay attention to src/main/resources/META-INF/services/org/apache/camel/TypeConverter
            // and com.redhat.training.camel.bookstore.PlainToFulfillmentOrderConverter
            .convertBodyTo(FulfillmentOrder.class)
            // enrich order data (just the book title and user ID) with data from the catalogs
            .enrich("direct:orderEnrichment")
            // finally, log the order...
            .to("log:orderLogger?showAll=true")
            // ...marshal to JSON...
            .marshal(new JacksonDataFormat())
            .to("log:orderAfterMarshal?showAll=true")
            // ...and write it to orders.json
            .to("file:data/out/?fileName=orders.json&fileExist=Append");

        // JSON2XML conversion
        // .to("xj:identity?transformDirection=JSON2XML")
        // .to("log:afterTransform");

        // order enrichment is just about two bean invocations
        // we could do it via routerSlip or just two consecutive bean() invocations
        from("direct:orderEnrichment")
            .routeId("orderEnrichment")
            // any of the business exceptions should redeliver up to 10 times
            // DO NOT mark those as handled(true) or they will propagate
            // also, do not get confused by the fact premature enrichment exceptions
            // remain in the exchange - that's just a record of history
            .onException(UserNotFoundException.class, BookNotFoundException.class)
                .to("log:orderEnrichmentERROR?showAll=true")
                .redeliveryDelay(1500)
                .maximumRedeliveries(10)
            .end()
            .to("log:orderEnrichmentPre?showAll=true")
            .bean(FulfillmentEnrichment.class, "enrichUser")
            .bean(FulfillmentEnrichment.class, "enrichBook")
            .to("log:orderEnrichmentPost?showAll=true");
    }
}
