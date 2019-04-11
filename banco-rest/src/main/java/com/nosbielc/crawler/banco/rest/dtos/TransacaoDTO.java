package com.nosbielc.crawler.banco.rest.dtos;

import com.nosbielc.crawler.banco.rest.enums.TipoTransacao;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

public class TransacaoDTO implements Serializable {

    private Long id;
    private String descricao;
    private Boolean ativo;
    private TipoTransacao tipoTransacao;
    private Float valorTransacao;
    private Date dateTimeTransferencia;
    private String conta;

    public TransacaoDTO() {
    }

    public TransacaoDTO(Long id, String descricao, Boolean ativo, TipoTransacao tipoTransacao, Float valorTransacao,
                        Date dateTimeTransferencia, String conta) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
        this.tipoTransacao = tipoTransacao;
        this.valorTransacao = valorTransacao;
        this.dateTimeTransferencia = dateTimeTransferencia;
        this.conta = conta;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransacaoDTO.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("descricao='" + descricao + "'")
                .add("ativo=" + ativo)
                .add("tipoTransacao=" + tipoTransacao)
                .add("valorTransacao=" + valorTransacao)
                .add("dateTimeTransferencia=" + dateTimeTransferencia)
                .add("conta='" + conta + "'")
                .toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public Float getValorTransacao() {
        return valorTransacao;
    }

    public void setValorTransacao(Float valorTransacao) {
        this.valorTransacao = valorTransacao;
    }

    public Date getDateTimeTransferencia() {
        return dateTimeTransferencia;
    }

    public void setDateTimeTransferencia(Date dateTimeTransferencia) {
        this.dateTimeTransferencia = dateTimeTransferencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }
}
