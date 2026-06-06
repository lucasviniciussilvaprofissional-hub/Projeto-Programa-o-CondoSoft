package com.condominio.service;

import com.condominio.models.area.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReservaService {

    // O 'static' impede que a lista seja zerada quando você muda de tela!
    private static final ObservableList<Reserva> listaReservas = FXCollections.observableArrayList();

    public void realizarReserva(Reserva reserva) {
        if (reserva != null) {
            listaReservas.add(reserva);
            System.out.println("[CondoSoft] Reserva salva no Service! Total na memória: " + listaReservas.size());
        }
    }

    public ObservableList<Reserva> listarReservas() {
        return listaReservas;
    }
}