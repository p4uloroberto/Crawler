package com.nosbielc.crawler.banco.rest.repositories;

import com.nosbielc.crawler.banco.rest.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
