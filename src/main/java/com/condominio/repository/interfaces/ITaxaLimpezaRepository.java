package com.condominio.repository.interfaces;

import com.condominio.models.area.TaxaLimpeza;
import java.util.List;

public interface ITaxaLimpezaRepository {
    void salvar(TaxaLimpeza taxa);
    List<TaxaLimpeza> listar();
    TaxaLimpeza buscarPorReserva(int reservaId);
    void atualizar(TaxaLimpeza taxa);
}
