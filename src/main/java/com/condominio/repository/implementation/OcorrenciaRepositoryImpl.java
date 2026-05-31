package com.condominio.repository.implementation;

import com.condominio.models.disciplina.Ocorrencia;
import com.condominio.repository.interfaces.IOcorrenciaRepository;

import java.util.ArrayList;
import java.util.List;

public class OcorrenciaRepositoryImpl implements IOcorrenciaRepository {

    // =========================================================================
    // O SEGREDO ESTÁ AQUI: A lista PRECISA ser 'static' para manter os dados
    // salvos na memória do programa entre as chamadas do Controller.
    // =========================================================================
    private static final List<Ocorrencia> listaOcorrencias = new ArrayList<>();

    @Override
    public void salvar(Ocorrencia ocorrencia) {
        if (ocorrencia != null) {
            listaOcorrencias.add(ocorrencia);
            System.out.println("[REPOSITÓRIO] Ocorrência salva com sucesso! Total na lista: " + listaOcorrencias.size());
        }
    }

    @Override
    public List<Ocorrencia> listar() {
        return new ArrayList<>(listaOcorrencias); // Retorna uma cópia segura da lista estática
    }
}