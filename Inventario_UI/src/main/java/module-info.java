module org.una.inventario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires com.jfoenix;
    requires lombok;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires jasperreports;
    requires java.desktop;
    requires com.opencsv;


    opens org.una.inventario to javafx.fxml;
    opens org.una.inventario.controller to javafx.fxml, javafx.controls, javafx.base, com.jfoenix;
    exports org.una.inventario;
    exports org.una.inventario.controller;
    exports org.una.inventario.dto to com.fasterxml.jackson.databind;
    opens org.una.inventario.data to javafx.base;
}