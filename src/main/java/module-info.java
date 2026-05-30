module com.example.projetoip {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.condominio to javafx.fxml;
    opens com.condominio.controller to javafx.fxml;
    opens com.condominio.controller.financeiro to javafx.fxml;

    exports com.condominio;
    opens com.condominio.controller.unidade to javafx.fxml;
    opens com.condominio.controller.reserva to javafx.fxml;
    exports com.condominio.controller;
}