package com.nosbielc.crawler.banco.rest.components;

import com.nosbielc.crawler.banco.rest.dtos.TransacaoDTO;
import com.nosbielc.crawler.banco.rest.entities.Transacao;
import org.springframework.stereotype.Component;

@Component
public class TransacaoNegocio {

    public TransacaoDTO transacaoToDto(Transacao transacao) {
        return new TransacaoDTO(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getAtivo(),
                transacao.getTipoTransacao(),
                transacao.getValorTransacao(),
                transacao.getDateTimeTransferencia(),
                transacao.getConta()
        );
    }
}
