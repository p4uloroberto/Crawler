package com.nosbielc.crawler.banco.soap.components;

import com.nosbielc.crawler.banco.soap.service.Transacao;
import com.nosbielc.crawler.banco.soap.util.GeradorCNPJ;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Component
public class TransacaoComponent {

    private static final List<Transacao> transacoes = new ArrayList<>();

    @PostConstruct
    public void initData() {

        for (int i = 0; i < 5000; i++) {

            try {
                Random rd = new Random();

                Transacao transacao = new Transacao();

                transacao.setAtivo(rd.nextInt(1));
                transacao.setId(i + 1);
                transacao.setConta(GeradorCNPJ.geraCNPJ());
                transacao.setTipoTransacao(rd.nextInt(2));
                transacao.setDescricao(String.format("Transação SOAP -> %s", i));
                transacao.setValorTransacao(BigDecimal.valueOf(rd.nextInt(999999)));
                transacao.setDateTimeTransferencia(Instant.now().toString());

                transacoes.add(transacao);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public List<Transacao> getAllTransacoes() {
        return transacoes;
    }

}
