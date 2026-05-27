package com.condominio.repository.implementation;

import java.util.ArrayList;
import java.util.List;

import com.condominio.models.finance.Pagamento;
import com.condominio.repository.interfaces.IPagamentoRepository;

public class PagamentoRepositoryImpl implements IPagamentoRepository {

    private final List<Pagamento> pagamentos = new ArrayList<>();

    @Override
    public void salvar(Pagamento pagamento) {
        pagamentos.add(pagamento);
    }

    @Override
    public List<Pagamento> listar() {
        return pagamentos;
    }

    @Override
    public Pagamento buscarPorId(int id) {

        for (Pagamento p : pagamentos) {

            if (p.getId() == id) {
                return p;
            }
        }

        return null;
    }

    @Override
    public List<Pagamento> buscarPorFormaPagamento(String formaPagamento) {

        List<Pagamento> resultado = new ArrayList<>();

        for (Pagamento p : pagamentos) {

            if (p.getFormaPagamento().equalsIgnoreCase(formaPagamento)) {
                resultado.add(p);
            }
        }

        return resultado;
    }

    @Override
    public void atualizar(Pagamento pagamento) {

        for (int i = 0; i < pagamentos.size(); i++) {

            if (pagamentos.get(i).getId() == pagamento.getId()) {

                pagamentos.set(i, pagamento);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        pagamentos.removeIf(p -> p.getId() == id);
    }
}
