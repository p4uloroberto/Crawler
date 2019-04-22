package com.nosbielc.crawler.camelexposesoapservice.beans;

import crawler.nosbielc.com.transacaoservice.types.GetTransacoesResponseType;
import crawler.nosbielc.com.transacaoservice.types.TransacaoType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetTransacoesBean {

    private static final Logger LOG = LoggerFactory.getLogger(GetTransacoesBean.class);

    // tag::main[]
    public static GetTransacoesResponseType process(List input) {

        // You would write some code here to populate the
        // list of workers, perhaps from a database.

        // Instead, to keep this example simple, we create a
        // simple response containing a list of workers.

        GetTransacoesResponseType response =
                new GetTransacoesResponseType();

        TransacaoType jane = new TransacaoType();
        jane.setFirstName("Jane");
        jane.setLastName("Smith");
        jane.setJobTitle("Manager");
        jane.setTransacaoID("1");
        response.getTransacao().add(jane);

        TransacaoType freddy = new TransacaoType();
        freddy.setFirstName("Freddy");
        freddy.setLastName("Cable");
        freddy.setJobTitle("Secretary");
        freddy.setTransacaoID("2");
        response.getTransacao().add(freddy);

        // Now we just return the response as a Java object.
        // We can leave CXF to do the XML conversion for us.

        return response;
    }
    // end::main[]

}
