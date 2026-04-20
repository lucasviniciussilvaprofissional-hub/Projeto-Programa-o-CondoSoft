package com.condominio.relatorio;

import java.time.LocalDateTime;
import com.condominio.finance.Inadimplencia;

public class ExportacaoInadimplentes {

    private int id;
    private Inadimplencia inadimplencia;
    private LocalDateTime dataExportacao;

    public ExportacaoInadimplentes() {
        this.dataExportacao = LocalDateTime.now();
    }

    public ExportacaoInadimplentes(int id, Inadimplencia inadimplencia) {
        this.id = id;
        this.inadimplencia = inadimplencia;
        this.dataExportacao = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Inadimplencia getInadimplencia() {
        return inadimplencia;
    }

    public void setInadimplencia(Inadimplencia inadimplencia) {
        this.inadimplencia = inadimplencia;
    }

    public LocalDateTime getDataExportacao() {
        return dataExportacao;
    }

    public void setDataExportacao(LocalDateTime dataExportacao) {
        this.dataExportacao = dataExportacao;
    }
}