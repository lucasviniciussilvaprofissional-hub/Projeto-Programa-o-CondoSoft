package com.condominio.repository.interfaces;

import com.condominio.models.comunicacao.Aviso;
import com.condominio.models.comunicacao.Edital;
import java.util.List;

public interface IComunicacaoRepository {
    void salvarAviso(Aviso aviso);
    void salvarEdital(Edital edital);
    List<Aviso> listarAvisos();
    List<Edital> listarEditais();
}