module com.example.projetoip {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jasperreports;
    requires java.sql;

    opens com.condominio to javafx.fxml;
    exports com.condominio.service;
    opens com.condominio.service;
    opens com.condominio.controller.unidade     to javafx.fxml;
    opens com.condominio.controller.financeiro   to javafx.fxml;
    opens com.condominio.controller.reserva      to javafx.fxml;
    exports com.condominio.controller.reserva;
    opens com.condominio.controller.comunicacao  to javafx.fxml;
    opens com.condominio.controller.disciplina   to javafx.fxml;
    opens com.condominio.controller.portaria     to javafx.fxml;
    opens com.condominio.controller.relatorio    to javafx.fxml;
    opens com.condominio.models.moradia          to javafx.base;

    exports com.condominio;
}