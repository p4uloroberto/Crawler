package com.nosbielc.crawler.banco.rest.service;

import com.nosbielc.crawler.banco.rest.entities.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface TransacaoService {

    Page<Transacao> findAllPageable(PageRequest pageRequest);

    Optional<Transacao> findById(Long id);

    Transacao persist(Transacao transacao);

    void remove(Transacao transacao);

    List<Transacao> findAll();

}
