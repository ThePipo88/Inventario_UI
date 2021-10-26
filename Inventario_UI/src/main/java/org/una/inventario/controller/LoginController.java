package org.una.inventario.controller;

import javafx.event.ActionEvent;
import org.una.inventario.util.FlowController;

public class LoginController extends Controller{

    @Override
    public void initialize() {

    }
    public void iniciar(ActionEvent actionEvent) {
        FlowController.getInstance().goViewInNewStage("Principal",stage);
    }
}
