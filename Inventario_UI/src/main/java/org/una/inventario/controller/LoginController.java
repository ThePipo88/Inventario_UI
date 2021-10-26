package org.una.inventario.controller;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.una.inventario.util.FlowController;

public class LoginController extends Controller{

    @Override
    public void initialize() {

    }

    public void iniciar(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInNewStage("Principal",stage);
    }
}
