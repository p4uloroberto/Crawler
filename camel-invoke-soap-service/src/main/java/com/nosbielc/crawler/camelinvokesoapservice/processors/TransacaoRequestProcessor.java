package com.nosbielc.crawler.camelinvokesoapservice.processors;

import crawler.nosbielc.com.transacaoservice.types.TransacaoType;
import crawler.nosbielc.com.transacaoservice.types.UpdateTransacaoRequestType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransacaoRequestProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        // Build our request payload, by creating a new object
        // of type UpdateWorkerRequestType, and then setting it in
        // the message body

        // The Body is an ArrayList containing our CSV values
        // We first get the body as a List<String>
        List<String> body = exchange.getIn().getBody(List.class);

        // Create our SOAP request object
        UpdateTransacaoRequestType request = new UpdateTransacaoRequestType();

        // Create a Worker that we will use in the SOAP request
        TransacaoType transacao = new TransacaoType();

        // Set fields in the Worker
        // from each column in the source CSV (numbered 0-4)
        transacao.setTransacaoID(body.get(0));
        transacao.setFirstName(body.get(1));
        transacao.setLastName(body.get(2));
        transacao.setJobTitle(body.get(3));
        transacao.setCountry(body.get(4));

        request.setTransacao(transacao);

        // Now place our SOAP request in the message body
        exchange.getIn().setBody(request);
    }

}
