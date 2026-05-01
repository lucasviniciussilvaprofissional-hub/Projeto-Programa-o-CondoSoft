package com.condominio.repository;

import com.condominio.area.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaRepositoryImpl implements ReservaRepository{


    private final List<Reserva> reservas = new ArrayList<>();

    @Override
    public void salvar(Reserva reserva) {
        reservas.add(reserva);
    }

    @Override
    public List<Reserva> listar() {
        return new ArrayList<>(reservas);
    }

    @Override
    public Reserva buscarPorId(int id) {
        for (Reserva r: reservas){
            if(r.getId() == id){
                return r;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Reserva reserva) {
        for (int i= 0; i< reservas.size();i++) {
            if(reservas.get(i).getId() == reserva.getId()){
                reservas.set(i,reserva);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        reservas.removeIf(r-> r.getId()==id);
    }

}
