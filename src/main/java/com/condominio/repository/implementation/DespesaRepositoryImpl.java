package com.condominio.repository.implementation;

import java.util.ArrayList;
import java.util.List;

import com.condominio.models.finance.Despesa;
import com.condominio.repository.interfaces.IReservaRepository;

public class DespesaRepositoryImpl implements IReservaRepository.IDespesaRepository {

    private final List<Despesa> despesas = new ArrayList<>();

    @Override
    public void salvar(Despesa despesa) {
        despesas.add(despesa);
    }

    @Override
    public List<Despesa> listar() {
        return despesas;
    }

    @Override
    public Despesa buscarPorId(int id) {

        for (Despesa d : despesas) {

            if (d.getId() == id) {
                return d;
            }
        }

        return null;
    }

    @Override
    public List<Despesa> buscarPorStatus(String status) {

        List<Despesa> resultado = new ArrayList<>();

        for (Despesa d : despesas) {

            if (d.getStatus().equalsIgnoreCase(status)) {
                resultado.add(d);
            }
        }

        return resultado;
    }

    @Override
    public List<Despesa> buscarPorTipo(String tipo) {

        List<Despesa> resultado = new ArrayList<>();

        for (Despesa d : despesas) {

            if (d.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(d);
            }
        }

        return resultado;
    }

    @Override
    public void atualizar(Despesa despesa) {

        for (int i = 0; i < despesas.size(); i++) {

            if (despesas.get(i).getId() == despesa.getId()) {

                despesas.set(i, despesa);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        despesas.removeIf(d -> d.getId() == id);
    }
}