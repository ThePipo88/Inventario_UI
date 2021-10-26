module org.una.inventario {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;

    opens org.una.inventario to javafx.fxml;
    exports org.una.inventario;
    exports org.una.inventario.controller;
}