package com.nosbielc.crawler.camelinvokesoapservice.routers;

import com.nosbielc.crawler.camelinvokesoapservice.processors.TransacaoRequestProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Autowired
    TransacaoRequestProcessor transacaoRequestProcessor;

    @Override
    public void configure() throws Exception {
        // Pick up a specific file (workers.csv) from our test data folder.
        // Setting "noop=true" means that Camel will do nothing to the file
        // once it's finished processing it.
        from("file:{{source.directory}}?fileName=transacoes.csv")
                .id("CapturaArquivoRouter::Internal")
                .description("Router resposanvel por buscar arquivos para processamento")
                .log("Arquivo Recebido -> ${body}")
                .to("direct:process");

        // Encapsulate all our logic behind a Direct endpoint
        from("direct:process")
                .id("InvokeSoapserviceRouter::Internal")
                .description("Router que realiza integração com SOAP Service")
                // Unmarshal the CSV into a Java object (a List)
                .unmarshal().csv()

                // Split the List and process each row
                .split()
                // This tells the Camel that it should use the most
                // natural method to split the body (i.e. by List elements)
                .body()

                // Tell Camel which SOAP operation we want to invoke.
                // This is set in our application.properties file
                .setHeader(CxfConstants.OPERATION_NAME,
                        simple("{{webservice.operation}}"))

                // Build our request payload, by creating a new object
                // of type UpdateWorkerRequestType, and then setting it in
                // the message body
                .process(transacaoRequestProcessor)

                .log("About to invoke SOAP service for transacao: "
                        + "FirstName = ${body.transacao.firstName}, "
                        + "LastName = ${body.transacao.lastName}")

                // Invoke the SOAP service, using the message body as the
                // request object
                // For the serviceClass option, we specify the interface
                // that was generated for us by cxf-codegen:wsdl2java
                .to("cxf://{{webservice.address}}"
                        + "?serviceClass=crawler.nosbielc.com.transacaoservice.Transacao"
                        + "&wsdlURL=/wsdl/transacaoservice.wsdl")

                // After invoking the SOAP service, Camel puts the response
                // into the Exchange body, as an object of type
                // org.apache.cxf.message.MessageContentsList.
                // The SOAP response object is the first item in this list.

                // We can use Camel's Simple language to get elements
                // from the response.
                .log("SOAP service returned status = ${body[0].status}")
                .end();
    }
}
