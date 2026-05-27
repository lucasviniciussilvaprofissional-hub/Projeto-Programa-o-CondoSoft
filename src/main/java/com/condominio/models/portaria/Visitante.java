package com.condominio.models.portaria;
import com.condominio.models.moradia.Unidade;

import java.time.LocalDateTime;
public class Visitante {

    private int id;
    private String nome;
    private String documento;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Unidade unidade;

    public Visitante (){
        this.dataEntrada = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }
    public void setDataEntrada(LocalDateTime dateEntrada){
        this.dataEntrada = dataEntrada;
    }
    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
}
