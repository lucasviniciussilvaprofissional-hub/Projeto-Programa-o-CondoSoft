package com.condominio.repository.implementation;

import com.condominio.models.area.Reserva;
import com.condominio.repository.interfaces.IReservaRepository;

import java.util.ArrayList;
import java.util.List;

public class ReservaRepositoryImpl implements IReservaRepository {

    // Modificado para static para manter os dados vivos entre a troca de telas do JavaFX
    private static final List<Reserva> reservas = new ArrayList<>();

    @Override
    public void salvar(Reserva reserva) {
        reservas.add(reserva);
        System.out.println("[Repository] Reserva salva com sucesso. Total na memória: " + reservas.size());
    }

    @Override
    public List<Reserva> listar() {
        return new ArrayList<>(reservas);
    }

    @Override
    public Reserva buscarPorId(int id) {
        for (Reserva r : reservas) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Reserva reserva) {
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == reserva.getId()) {
                reservas.set(i, reserva);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        reservas.removeIf(r -> r.getId() == id);
    }
}