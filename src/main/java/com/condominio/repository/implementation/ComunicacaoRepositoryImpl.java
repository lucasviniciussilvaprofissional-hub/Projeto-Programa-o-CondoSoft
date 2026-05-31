package com.condominio.repository.implementation;

import com.condominio.models.comunicacao.Aviso;
import com.condominio.models.comunicacao.Edital;
import com.condominio.repository.interfaces.IComunicacaoRepository;

import java.util.ArrayList;
import java.util.List;

public class ComunicacaoRepositoryImpl implements IComunicacaoRepository {

    private static final List<Aviso> listaAvisos = new ArrayList<>();
    private static final List<Edital> listaEditais = new ArrayList<>();

    @Override
    public void salvarAviso(Aviso aviso) {
        if (aviso != null) {
            listaAvisos.add(aviso);
        }
    }

    @Override
    public void salvarEdital(Edital edital) {
        if (edital != null) {
            listaEditais.add(edital);
        }
    }

    @Override
    public List<Aviso> listarAvisos() {
        return new ArrayList<>(listaAvisos);
    }

    @Override
    public List<Edital> listarEditais() {
        return new ArrayList<>(listaEditais);
    }
}