package com.condominio.repository.interfaces;

import com.condominio.models.moradia.Unidade;
import java.util.List;

public interface IUnidadeRepository {

    void salvar (Unidade unidade);
    List<Unidade> listar();


    Unidade buscarPorId(int id);

    void atualizar (Unidade unidade);
    void remover(int id);

}
