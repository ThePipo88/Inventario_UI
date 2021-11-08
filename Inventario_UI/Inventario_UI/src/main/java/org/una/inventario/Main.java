package org.una.inventario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.una.inventario.util.FlowController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        stage.setTitle("Control de inventarios");
        FlowController.getInstance().goViewInWindow("Login");
    }

    public static void main(String[] args) {
        launch();
    }
}