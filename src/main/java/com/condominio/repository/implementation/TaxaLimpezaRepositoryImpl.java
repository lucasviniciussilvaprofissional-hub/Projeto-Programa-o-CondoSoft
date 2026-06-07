package com.condominio.repository.implementation;

import com.condominio.models.area.TaxaLimpeza;
import com.condominio.repository.interfaces.ITaxaLimpezaRepository;

import java.util.ArrayList;
import java.util.List;

public class TaxaLimpezaRepositoryImpl implements ITaxaLimpezaRepository {

    private static final List<TaxaLimpeza> taxas = new ArrayList<>();

    @Override public void salvar(TaxaLimpeza t) { taxas.add(t); }
    @Override public List<TaxaLimpeza> listar() { return new ArrayList<>(taxas); }

    @Override
    public TaxaLimpeza buscarPorReserva(int reservaId) {
        for (TaxaLimpeza t : taxas)
            if (t.getReserva() != null && t.getReserva().getId() == reservaId) return t;
        return null;
    }

    @Override
    public void atualizar(TaxaLimpeza taxa) {
        for (int i = 0; i < taxas.size(); i++)
            if (taxas.get(i).getId() == taxa.getId()) { taxas.set(i, taxa); return; }
    }
}
