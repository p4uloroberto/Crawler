package com.nosbielc.crawler.bancodatarest.repositories;

import com.nosbielc.crawler.bancodatarest.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "transacao", path = "transacao")
public interface TransacaoDataRestRepository extends PagingAndSortingRepository<Transacao, Long> {

    List<Transacao> findByIdOrderByIdAsc(@Param("id") Long id);

    List<Transacao> findByAtivoOrderByIdAsc(@Param("ativo") Boolean ativo);
}
