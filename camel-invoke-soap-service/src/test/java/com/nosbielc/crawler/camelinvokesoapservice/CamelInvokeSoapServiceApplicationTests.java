package com.nosbielc.crawler.camelinvokesoapservice;

import crawler.nosbielc.com.transacaoservice.types.UpdateTransacaoRequestType;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = CamelInvokeSoapServiceApplication.class,
        properties = "source.directory = target/files")
@MockEndpointsAndSkip("cxf:*")
public class CamelInvokeSoapServiceApplicationTests {

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate template;

    @Test
    public void testRoute() throws Exception {
        // Get our CXF endpoint which has been turned into a MockEndpoint
        MockEndpoint mock = camelContext.getEndpoint(
                "mock:cxf:{{webservice.address}}",
                MockEndpoint.class);

        // Expect that we will send 4 messages to this MockEndpoint
        mock.expectedMessageCount(3);

        // Send a sample data file to the starting endpoint
        // We explicitly craft a message body (the second argument)
        // and then set the output file name to be 'workers.csv'
        template.sendBodyAndHeader("file:{{source.directory}}",
                "1,Dave,Smith,Chief Executive,GB\n" +
                        "2,Bobby,Farquhar,Senior Manager,GB\n" +
                        "3,Lucinda,Barker,Senior Manager,US",
                Exchange.FILE_NAME, "transacoes.csv");

        // Waits for 4 messages to arrive at the MockEndpoint
        mock.assertIsSatisfied();

        // You can perform individual assertions on messages too!
        // Here checking that the first worker's name is 'Dave'
        assertEquals("Dave",
                mock.getExchanges().get(0).getIn()
                        .getBody(UpdateTransacaoRequestType.class)
                        .getTransacao().getFirstName());
    }

}
