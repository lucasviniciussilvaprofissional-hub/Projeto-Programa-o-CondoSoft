package com.condominio.repository.interfaces;

import com.condominio.enums.StatusBoleto;
import com.condominio.models.finance.Boleto;
import java.util.List;

public interface IBoletoRepository {
    void salvar(Boleto boleto);
    List<Boleto> listar();
    Boleto buscarPorId(int id);
    void atualizar(Boleto boleto);
    void remover(int id);
    List<Boleto> listarPorUnidade(int unidadeId);
    List<Boleto> listarPorStatus(StatusBoleto status);
}
