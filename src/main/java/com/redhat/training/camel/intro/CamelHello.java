package com.redhat.training.camel.intro;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelHello extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // read all files from the incoming directory except .html (regex: .*\.html)
        from("file:data/incoming/?noop=true&exclude=.*\\.html")
            // turn on tracing (don't forget to run with logging.level.root=TRACE)
            .tracing("true")
            // set a meaningful route ID
            .routeId("FileReader")
            // CamelFileName is the message header that reflects current file being read
            .log("Processing file ${header.CamelFileName}...")
            // append .txt to file name using a processor
            .process(new FileNameProcessor())
            // append .txt to file name using a bean (first would throw AmbiguousMethodCallException)
            // .bean(ChangeFileName.class)
            .bean(ChangeFileName.class, "changeFileName")
            // this will set two headers: format and size, invoking the DetectFileType bean twice
            .routingSlip(constant("bean:DetectFileType?method=getFileFormat,bean:DetectFileType?method=getFileSize"))
            // use the above headers to select destination directory
            .toD("file:data/out/${header.size}/${header.format}");

            // you can also invoke routingSlip from message headers - statically (next line) or dynamically (line after that)
            // .setHeader("processorList").constant("bean:MyContentInspector,log:foo?showAll=true,jms:topic:xyz")
            // .setHeader("processorList").method(ContentInspectorDecisionMaker.class)
            // .routingSlip(header("processorList"))

            // dynamicRouter is recursive invocation of a bean method or processor until it returns null
            // .dynamicRouter(method(RecursiveDestinationComposer.class, "figureOutNextProcessor"))

            // toD is a dynamic processor/producer instantiation mechanism
            // .bean(NextStepCalculater.class)
            // .toD("${header.nextStep}")

            // CBR example: send files to data/out/${class}, where class is one of small, medium, large, xlarge
            // .choice()
            //     .when(simple("${header.CamelFileLength} < 10000"))
            //         .to("file:data/out/small/")
            //     .when(simple("${header.CamelFileLength} >= 10000 && ${header.CamelFileLength} < 50000"))
            //         .to("file:data/out/medium/")
            //     .when(simple("${header.CamelFileLength} >= 50000 && ${header.CamelFileLength} < 100000"))
            //         .to("file:data/out/large/")
            //     .otherwise()
            //         .to("file:data/out/xlarge/")
            // .end()
            // .to("log:afterChoice");

        // if necessary, you can stop a route via a route controller
        // getContext().getRouteController().stopRoute("FileReader");
    }
}
