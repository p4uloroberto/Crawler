package com.nosbielc.crawler.camelapidb.exception;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class ExceptionProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        System.out.println("Exceptipn Message "  + e.getMessage());
        System.out.println("Exceptipn Class "  + e.getClass());

        String failedEndoint = (String) exchange.getProperty(Exchange.FAILURE_ENDPOINT);
        System.out.println("Endpoint com falha: " + failedEndoint);

        exchange.getIn().setBody("Exceção aconteceu na rota.");
    }

}
