package com.nosbielc.crawler.camelexposesoapservice.router;

import com.nosbielc.crawler.camelexposesoapservice.beans.GetTransacoesBean;
import com.nosbielc.crawler.camelexposesoapservice.beans.UpdateTransacaoBean;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Router extends RouteBuilder {

    @Autowired
    GetTransacoesBean getTransacoesBean;

    @Autowired
    UpdateTransacaoBean updateTransacaoBean;

    @Override
    public void configure() {

        // tag::route[]
        // This Camel route uses the CXF component as a Producer
        // So it publishes a CXF SOAP web service
        // Any requests received will go through this route
        from("cxf:"
                // URL path for the SOAP service
                + "/TransacaoService"
                // Set the internal path to the service WSDL
                + "?wsdlURL=/wsdl/transacaoservice.wsdl"
                // Set the package containing our JAXB classes
                + "&serviceClass="
                + "crawler.nosbielc.com.transacaoservice.Transacao")

                .log("Received request!")

                // Check to see which SOAP operation was invoked
                .choice()
                .when(header(CxfConstants.OPERATION_NAME)
                        .isEqualTo("UpdateTransacao"))
                // Pass to a Java class for processing.
                // This class also creates the response object
                .bean(updateTransacaoBean)

                .when(header(CxfConstants.OPERATION_NAME)
                        .isEqualTo("GetTransacoes"))
                // Pass to a Java class for processing.
                .bean(getTransacoesBean)

                .end()

                // Nothing left to do here! Whatever is now in the
                // Exchange Body will be returned to the consumer

                .log("Returning response!");
        // end::route[]

    }

}
