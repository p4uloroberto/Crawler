package com.nosbielc.crawler.camelexposesoapservice.beans;

import crawler.nosbielc.com.transacaoservice.Transacao;
import crawler.nosbielc.com.transacaoservice.types.TransacaoType;
import crawler.nosbielc.com.transacaoservice.types.UpdateTransacaoRequestType;
import crawler.nosbielc.com.transacaoservice.types.UpdateTransacaoResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateTransacaoBean {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateTransacaoBean.class);

    // tag::main[]
    public static UpdateTransacaoResponseType process(List input) {
        // CXF places the SOAP request message inside a List,
        // so we get the first element from the List
        UpdateTransacaoRequestType request =
                (UpdateTransacaoRequestType) input.get(0);

        // The request is actually a simple Java object
       TransacaoType worker = request.getTransacao();

        // Write a helpful message to the log
        // This is how you access the fields in the request
        LOG.info("Estou processando uma transacao - {}, {}, {}",
                worker.getTransacaoID(),
                worker.getFirstName(),
                worker.getLastName());

        // You can add code here to process the incoming object

        // Create a new instance of the Response object
        UpdateTransacaoResponseType response =
                new UpdateTransacaoResponseType();

        // Set the 'Status' element inside the Response object
        response.setStatus("OK");

        return response;
    }
    // end::main[]

}
