package com.nosbielc.crawler.banco.rest.service;

import com.nosbielc.crawler.banco.rest.entities.Transacao;
import com.nosbielc.crawler.banco.rest.repositories.TransacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoServiceProvider implements TransacaoService {

    private static final Logger log = LoggerFactory.getLogger(TransacaoServiceProvider.class);

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Override
    public Page<Transacao> findAllPageable(PageRequest pageRequest) {
        return this.transacaoRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Transacao> findById(Long id) {
        return this.transacaoRepository.findById(id);
    }

    @Override
    public Transacao persist(Transacao transacao) {
        return this.transacaoRepository.save(transacao);
    }

    @Override
    public void remove(Transacao transacao) {
        this.transacaoRepository.delete(transacao);
    }

    @Override
    public List<Transacao> findAll() {
        return this.transacaoRepository.findAll();
    }
}
