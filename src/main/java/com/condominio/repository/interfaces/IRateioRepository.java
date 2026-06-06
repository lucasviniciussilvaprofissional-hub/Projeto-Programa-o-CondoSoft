package com.condominio.repository.interfaces;

import com.condominio.models.finance.Rateio;
import java.util.List;

public interface IRateioRepository {
    void salvar(Rateio rateio);
    List<Rateio> listar();
    List<Rateio> listarPorUnidade(int unidadeId);
    List<Rateio> listarPorCompetencia(String competencia);
    void remover(int id);
}
