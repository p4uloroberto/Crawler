package com.nosbielc.crawler.banco.rest.controllers;

import com.nosbielc.crawler.banco.rest.components.TransacaoNegocio;
import com.nosbielc.crawler.banco.rest.dtos.TransacaoDTO;
import com.nosbielc.crawler.banco.rest.entities.Transacao;
import com.nosbielc.crawler.banco.rest.response.Response;
import com.nosbielc.crawler.banco.rest.service.TransacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transacao")
@CrossOrigin(origins = "*")
public class TransacaoController {

    private static final Logger log = LoggerFactory.getLogger(TransacaoController.class);

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private TransacaoNegocio transacaoNegocio;

    @Value("${pagination.qtd_by_page}")
    private int fetchForPage;

    @GetMapping("/pageable")
    public ResponseEntity<Response<Page<TransacaoDTO>>> listar(@RequestParam(value = "pag", defaultValue = "0") Integer pag,
                                                         @RequestParam(value = "ord", defaultValue = "id") String ord,
                                                         @RequestParam(value = "dir", defaultValue = "DESC") String dir) {
        Response<Page<TransacaoDTO>> response = new Response<>();
        PageRequest pageRequest = PageRequest.of(pag, this.fetchForPage, Sort.Direction.valueOf(dir), ord);
        Page<Transacao> transacoes = this.transacaoService.findAllPageable(pageRequest);
        Page<TransacaoDTO> transacaoDto = transacoes.map(
                transacao -> this.transacaoNegocio.transacaoToDto(transacao)
        );

        response.setData(transacaoDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<TransacaoDTO>> show(@PathVariable(value = "id") Long id) {
        Response<TransacaoDTO> response = new Response<>();
        Optional<Transacao> transacao = this.transacaoService.findById(id);

        if (transacao.isPresent()) {
            response.setData(this.transacaoNegocio.transacaoToDto(transacao.get()));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TransacaoDTO>> all() {
        List<Transacao> transacoes = this.transacaoService.findAll();

        List<TransacaoDTO> transacaoDto = new ArrayList<>();

        transacoes.stream().forEach(transacao -> {
            transacaoDto.add(this.transacaoNegocio.transacaoToDto(transacao));
        });

        return ResponseEntity.ok(transacaoDto);
    }

}
