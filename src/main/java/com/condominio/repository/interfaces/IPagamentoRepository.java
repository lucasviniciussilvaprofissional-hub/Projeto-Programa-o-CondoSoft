package com.condominio.repository.interfaces;

import com.condominio.models.finance.Pagamento;
import java.util.List;

public interface IPagamentoRepository {

    void salvar(Pagamento pagamento);

    List<Pagamento> listar();

    Pagamento buscarPorId(int id);

    List<Pagamento> buscarPorFormaPagamento(String formaPagamento);

    void atualizar(Pagamento pagamento);

    void remover(int id);
}