package com.nosbielc.crawler.camelapidb.model;

public enum TipoTransacao {
    CREDITO(0),
    DEBITO(1),
    ACERTO(2),
    DESCONHECIDO(100);

    private int key;

    TipoTransacao(int key) {
        this.key = key;
    }

    public static TipoTransacao getByValor(int key) {
        for (TipoTransacao tt : values()) {
            if (tt.getKey() == key) {
                return tt;
            }
        }
        return TipoTransacao.DESCONHECIDO;
    }

    public int getKey() {
        return key;
    }

}
