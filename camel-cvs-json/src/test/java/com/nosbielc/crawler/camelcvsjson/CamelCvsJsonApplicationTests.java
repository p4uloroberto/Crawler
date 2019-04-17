package com.nosbielc.crawler.camelcvsjson;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "uri.output = mock:output"
})
@UseAdviceWith
public class CamelCvsJsonApplicationTests {

    @EndpointInject(uri = "mock:output")
    MockEndpoint output;

    @Produce(uri = "direct:process")
    ProducerTemplate process;

    @Autowired
    CamelContext context;

    @Test
    public void validJson() throws Exception {
        String expected = "{\n" +
                "  \" Conta\" : \"1\",\n" +
                "  \"Descrição\" : \"1\",\n" +
                "  \" ValorTransação\" : \"2\",\n" +
                "  \" Ativo\" : \"2019-04-17T15:50:24.625+0000\",\n" +
                "  \" DataTransação\" : \"5702052\",\n" +
                "  \" TipoTransação\" : \"'Descricao Transação 43374914889'\"\n" +
                "}";

        // Esperamos que a primeira mensagem de saída seja a mesma que a string acima
        output.message(0).body().convertToString().isEqualTo(expected);

        // Agora enviamos uma mensagem de teste para seu endpoint direto: process
        process.sendBody("Descrição, TipoTransação, ValorTransação, DataTransação, Ativo, Conta\n" +
                "1,'Descricao Transação 43374914889',2,5702052,2019-04-17T15:50:24.625+0000,1,'42161905848816'");

        // Assert que o nosso ponto final Mock está satisfeito (que a mensagem é a esperada)
        output.assertIsSatisfied();
    }

}
