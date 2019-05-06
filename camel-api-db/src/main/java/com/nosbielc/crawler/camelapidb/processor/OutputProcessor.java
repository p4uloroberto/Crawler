package com.nosbielc.crawler.camelapidb.processor;

import com.nosbielc.crawler.camelapidb.model.TipoTransacao;
import com.nosbielc.crawler.camelapidb.model.Transacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OutputProcessor {

    public static  List<Transacao> processTransacoes(List<Map> dbRecords) {
        List<Transacao> trans = new ArrayList<>();

        dbRecords
                .stream()
                .forEach(record -> {

                    Transacao transacao = new Transacao();
                    transacao.setId((Long) record.get("ID"));
                    transacao.setAtivo( ((Integer)record.get("IS_ATIVO") == 0) ? Boolean.FALSE : Boolean.TRUE );
                    transacao.setConta((String) record.get("CONTA"));
                    transacao.setDateTimeTransferencia((Date) record.get("dt_transacao"));
                    transacao.setDescricao((String) record.get("DESCRICAO"));
                    transacao.setTipoTransacao(TipoTransacao.getByValor((Integer) record.get("TIPO_TRANSACAO")) );
                    transacao.setValorTransacao(((BigDecimal) record.get("VLR_TRANSACAO")).floatValue());

                    trans.add(transacao);
                });

        return trans;
    }

}
