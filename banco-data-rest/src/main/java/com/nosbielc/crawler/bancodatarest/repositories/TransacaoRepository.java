package com.nosbielc.crawler.bancodatarest.repositories;

import com.nosbielc.crawler.bancodatarest.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
