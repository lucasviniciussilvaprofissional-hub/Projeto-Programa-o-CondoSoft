package com.condominio.repository.implementation;

import com.condominio.enums.StatusDespesa;
import com.condominio.models.finance.Despesa;
import com.condominio.repository.interfaces.IDespesaRepository;

import java.util.ArrayList;
import java.util.List;

public class DespesaRepositoryImpl implements IDespesaRepository {

    private static final List<Despesa> despesas = new ArrayList<>();

    @Override public void salvar(Despesa d)   { despesas.add(d); }
    @Override public List<Despesa> listar()   { return new ArrayList<>(despesas); }
    @Override public void remover(int id)     { despesas.removeIf(d -> d.getId() == id); }

    @Override
    public Despesa buscarPorId(int id) {
        for (Despesa d : despesas) if (d.getId() == id) return d;
        return null;
    }

    @Override
    public List<Despesa> buscarPorStatus(StatusDespesa status) {
        List<Despesa> r = new ArrayList<>();
        for (Despesa d : despesas)
            if (d.getStatus() == status) r.add(d);
        return r;
    }

    @Override
    public List<Despesa> buscarPorTipo(String tipo) {
        List<Despesa> r = new ArrayList<>();
        for (Despesa d : despesas)
            if (d.getTipo() != null && d.getTipo().name().equalsIgnoreCase(tipo)) r.add(d);
        return r;
    }

    @Override
    public void atualizar(Despesa despesa) {
        for (int i = 0; i < despesas.size(); i++)
            if (despesas.get(i).getId() == despesa.getId()) { despesas.set(i, despesa); return; }
    }
}
