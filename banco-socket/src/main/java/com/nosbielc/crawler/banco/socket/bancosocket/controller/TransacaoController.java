package com.nosbielc.crawler.banco.socket.bancosocket.controller;
import com.nosbielc.crawler.banco.socket.bancosocket.entities.Transacao;
import com.nosbielc.crawler.banco.socket.bancosocket.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class TransacaoController {

    @Autowired
    TransacaoRepository trRepo;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<Transacao> greeting() throws Exception {
        Thread.sleep(1000); //
        return this.trRepo.findAll();
    }

}
