package com.condominio.repository.implementation;

import com.condominio.models.finance.Rateio;
import com.condominio.repository.interfaces.IRateioRepository;

import java.util.ArrayList;
import java.util.List;

public class RateioRepositoryImpl implements IRateioRepository {

    private static final List<Rateio> rateios = new ArrayList<>();

    @Override public void salvar(Rateio r)     { rateios.add(r); }
    @Override public List<Rateio> listar()     { return new ArrayList<>(rateios); }
    @Override public void remover(int id)      { rateios.removeIf(r -> r.getId() == id); }

    @Override
    public List<Rateio> listarPorUnidade(int unidadeId) {
        List<Rateio> res = new ArrayList<>();
        for (Rateio r : rateios)
            if (r.getUnidade() != null && r.getUnidade().getId() == unidadeId) res.add(r);
        return res;
    }

    @Override
    public List<Rateio> listarPorCompetencia(String competencia) {
        List<Rateio> res = new ArrayList<>();
        for (Rateio r : rateios)
            if (competencia.equals(r.getCompetencia())) res.add(r);
        return res;
    }
}
