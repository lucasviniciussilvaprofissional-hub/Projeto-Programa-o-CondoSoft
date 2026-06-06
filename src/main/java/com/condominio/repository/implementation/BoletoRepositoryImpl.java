package com.condominio.repository.implementation;

import com.condominio.enums.StatusBoleto;
import com.condominio.models.finance.Boleto;
import com.condominio.repository.interfaces.IBoletoRepository;

import java.util.ArrayList;
import java.util.List;

public class BoletoRepositoryImpl implements IBoletoRepository {

    private static final List<Boleto> boletos = new ArrayList<>();

    @Override public void salvar(Boleto boleto)   { boletos.add(boleto); }
    @Override public List<Boleto> listar()        { return new ArrayList<>(boletos); }
    @Override public void remover(int id)         { boletos.removeIf(b -> b.getId() == id); }

    @Override
    public Boleto buscarPorId(int id) {
        for (Boleto b : boletos) if (b.getId() == id) return b;
        return null;
    }

    @Override
    public void atualizar(Boleto boleto) {
        for (int i = 0; i < boletos.size(); i++)
            if (boletos.get(i).getId() == boleto.getId()) { boletos.set(i, boleto); return; }
    }

    @Override
    public List<Boleto> listarPorUnidade(int unidadeId) {
        List<Boleto> r = new ArrayList<>();
        for (Boleto b : boletos)
            if (b.getUnidade() != null && b.getUnidade().getId() == unidadeId) r.add(b);
        return r;
    }

    @Override
    public List<Boleto> listarPorStatus(StatusBoleto status) {
        List<Boleto> r = new ArrayList<>();
        for (Boleto b : boletos) if (b.getStatus() == status) r.add(b);
        return r;
    }
}
