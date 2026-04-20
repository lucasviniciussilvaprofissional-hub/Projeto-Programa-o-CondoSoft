package com.condominio.service;

import java.util.List;
import com.condominio.model.Unidade;
import com.condominio.model.Morador;
import com.condominio.area.Reserva;
import com.condominio.finance.Boleto;

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


    public void adicionarUnidade(Unidade unidade) {}

    public void adicionarMorador(Morador morador, Unidade unidade) {}


    public void realizarReserva(Reserva reserva) {}


    public Boleto gerarBoleto(Unidade unidade) {
        return null;
    }

    public void registrarPagamento(Boleto boleto) {}

}