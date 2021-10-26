package org.una.inventario.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

public class Mensaje {

    public void show(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void showModal(Alert.AlertType tipo, String titulo, Window padre, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.initOwner(padre);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public Boolean showConfirmation(String titulo, Window padre, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.initOwner(padre);
        alert.setContentText(mensaje);
        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }
}
