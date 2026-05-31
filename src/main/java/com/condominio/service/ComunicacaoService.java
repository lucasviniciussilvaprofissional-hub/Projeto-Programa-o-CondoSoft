package com.condominio.service;

import com.condominio.models.comunicacao.Aviso;
import com.condominio.models.comunicacao.Edital;
import com.condominio.repository.implementation.ComunicacaoRepositoryImpl;
import com.condominio.repository.interfaces.IComunicacaoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ComunicacaoService {

    private final IComunicacaoRepository repository = new ComunicacaoRepositoryImpl();

    public void publicarAviso(String titulo, String mensagem) {
        int idGerado = (int) (System.currentTimeMillis() & 0xfffffff);

        Aviso novoAviso = new Aviso();
        novoAviso.setId(idGerado);
        novoAviso.setTitulo(titulo);
        novoAviso.setMensagem(mensagem);
        novoAviso.setPrioridade("NORMAL");

        repository.salvarAviso(novoAviso);
    }

    public void publicarEdital(String titulo, String descricao) {
        int idGerado = (int) (System.currentTimeMillis() & 0xfffffff);
        // Define a validade padrão do Edital para 30 dias a partir de hoje
        LocalDateTime validadePadrao = LocalDateTime.now().plusDays(30);

        Edital novoEdital = new Edital(idGerado, titulo, descricao, validadePadrao);
        repository.salvarEdital(novoEdital);
    }

    public List<Aviso> obterAvisos() {
        return repository.listarAvisos();
    }

    public List<Edital> obterEditais() {
        return repository.listarEditais();
    }
}