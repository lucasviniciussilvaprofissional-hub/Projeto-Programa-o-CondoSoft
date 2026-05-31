package com.condominio.repository.interfaces;

import com.condominio.models.disciplina.Ocorrencia;
import java.util.List;

public interface IOcorrenciaRepository {

    // Método para salvar uma nova ocorrência na lista
    void salvar(Ocorrencia ocorrencia);

    // Método que retorna todas as ocorrências registradas para a tabela
    List<Ocorrencia> listar();
}