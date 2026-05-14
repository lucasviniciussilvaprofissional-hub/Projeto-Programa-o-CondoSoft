package com.condominio.repository.interfaces;

import com.condominio.models.finance.Despesa;
import java.util.List;

public interface IDespesaRepository {

    void salvar(Despesa despesa);

    List<Despesa> listar();

    Despesa buscarPorId(int id);

    List<Despesa> buscarPorStatus(String status);

    List<Despesa> buscarPorTipo(String tipo);

    void atualizar(Despesa despesa);

    void remover(int id);
}