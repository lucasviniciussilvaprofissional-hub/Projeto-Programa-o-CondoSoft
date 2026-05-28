package com.condominio.controller;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;

public class ListarUnidadeController {

    // HEADER
    public void abrirCadastrarUnidade(ActionEvent event) {
        System.out.println("Abrir cadastrar unidade");
    }

    public void voltarUnidades(ActionEvent event) {
        System.out.println("Voltar unidades");
    }

    // BREADCRUMB
    public void voltarHome() {
        System.out.println("Voltar home");
    }

    // FILTROS
    public void filtrarUnidades() {
        System.out.println("Filtrar unidades");
    }

    // caso o JavaFX reclame do onKeyReleased
    public void filtrarUnidades(KeyEvent event) {
        System.out.println("Filtrar unidades");
    }

    public void limparFiltros(ActionEvent event) {
        System.out.println("Limpar filtros");
    }

    // AÇÕES DA TABELA
    public void abrirDetalhesUnidade(ActionEvent event) {
        System.out.println("Abrir detalhes");
    }

    public void editarUnidade(ActionEvent event) {
        System.out.println("Editar unidade");
    }

    public void confirmarRemocaoUnidade(ActionEvent event) {
        System.out.println("Remover unidade");
    }
}