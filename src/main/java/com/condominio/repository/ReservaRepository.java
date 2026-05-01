package com.condominio.repository;

import com.condominio.area.Reserva;

import java.util.List;

public interface ReservaRepository {

    void salvar(Reserva reserva);

    List<Reserva> listar();

    Reserva buscarPorId(int id);

    void atualizar(Reserva reserva);

    void remover(int id);

}
