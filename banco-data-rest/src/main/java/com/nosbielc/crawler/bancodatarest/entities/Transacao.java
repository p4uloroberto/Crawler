package com.nosbielc.crawler.bancodatarest.entities;

import com.nosbielc.crawler.bancodatarest.enums.TipoTransacao;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

@Entity
@Table( name = "transacao")
public class Transacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "is_ativo", nullable = false)
    private Boolean ativo;

    @Enumerated(EnumType.ORDINAL)
    private TipoTransacao tipoTransacao;

    @Column(name = "vlr_transacao", nullable = false, precision = 2)
    private Float valorTransacao;

    @Column(name = "dt_transacao", nullable = false)
    @CreatedBy
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeTransferencia;

    @Column(name = "conta", nullable = false)
    private String conta;

    public Transacao() {
    }

    public Transacao(String descricao, Boolean ativo, TipoTransacao tipoTransacao, Float valorTransacao,
                     Date dateTimeTransferencia, String conta) {
        this.descricao = descricao;
        this.ativo = ativo;
        this.tipoTransacao = tipoTransacao;
        this.valorTransacao = valorTransacao;
        this.dateTimeTransferencia = dateTimeTransferencia;
        this.conta = conta;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Transacao.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("descricao='" + descricao + "'")
                .add("ativo=" + ativo)
                .add("tipoTransacao=" + tipoTransacao)
                .add("valorTransacao=" + valorTransacao)
                .add("dateTimeTransferencia=" + dateTimeTransferencia)
                .add("conta='" + conta + "'")
                .toString();
    }
}
