package com.condominio.repository;

import com.condominio.models.moradia.Unidade;
import java.util.List;

public interface UnidadeRepository {

    void salvar (Unidade unidade);
    List<Unidade> listar();


    Unidade buscarPorId(int id);

    void atualizar (Unidade unidade);
    void remover(int id);

}
