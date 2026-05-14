package com.condominio.service;

import com.condominio.models.area.Reserva;
import com.condominio.models.finance.Boleto;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;


import java.util.List;

public class CondominioService {

    private List<Unidade> unidades;
    private List<Morador> moradores;
    private List<Reserva> reservas;
    private List<Boleto> boletos;

    public CondominioService(List<Unidade> unidades,
                             List<Morador> moradores,
                             List<Reserva> reservas,
                             List<Boleto> boletos) {

        this.unidades = unidades;
        this.moradores = moradores;
        this.reservas = reservas;
        this.boletos = boletos;
    }


    public void adicionarUnidade(Unidade unidade) {

        unidades.add(unidade);
    }


    public void adicionarMorador(Morador morador,
                                 Unidade unidade) {

        moradores.add(morador);

        unidade.adicionarMorador(morador);
    }


    public void realizarReserva(Reserva reserva) {

        reservas.add(reserva);
    }



    public void registrarPagamento(Boleto boleto) {

        boleto.setStatus("PAGO");
    }


    public List<Unidade> listarUnidades() {

        return unidades;
    }


    public List<Morador> listarMoradores() {

        return moradores;
    }


    public List<Reserva> listarReservas() {

        return reservas;
    }


    public List<Boleto> listarBoletos() {

        return boletos;
    }

}