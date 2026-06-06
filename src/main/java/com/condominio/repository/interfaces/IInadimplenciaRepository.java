package com.condominio.repository.interfaces;

import com.condominio.models.finance.Inadimplencia;
import java.util.List;

public interface IInadimplenciaRepository {

    void salvar(Inadimplencia inadimplencia);

    List<Inadimplencia> listar();

    Inadimplencia buscarPorId(int id);

    void atualizar(Inadimplencia inadimplencia);

    void remover(int id);
}