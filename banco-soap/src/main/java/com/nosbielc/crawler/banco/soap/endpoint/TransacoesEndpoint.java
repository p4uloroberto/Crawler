package com.nosbielc.crawler.banco.soap.endpoint;

import com.nosbielc.crawler.banco.soap.components.TransacaoComponent;
import com.nosbielc.crawler.banco.soap.service.GetAllTransacoesRequest;
import com.nosbielc.crawler.banco.soap.service.GetAllTransacoesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TransacoesEndpoint {

    private static final String NAMESPACE_URI = "http://com.nosbielc.crawler/banco/soap-web-service";

    private TransacaoComponent transacaoComponent;

    @Autowired
    public TransacoesEndpoint(TransacaoComponent transacaoComponent) {
        this.transacaoComponent = transacaoComponent;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllTransacoesRequest")
    @ResponsePayload
    public GetAllTransacoesResponse getAllTransacoes(@RequestPayload GetAllTransacoesRequest request) {
        GetAllTransacoesResponse response = new GetAllTransacoesResponse();
        response.setTransacao(transacaoComponent.getAllTransacoes());
        return response;
    }

}
