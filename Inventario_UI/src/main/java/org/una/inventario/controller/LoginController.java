package org.una.inventario.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.una.inventario.dto.AuthenticationResponse;
import org.una.inventario.service.LoginService;
import org.una.inventario.util.FlowController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginController extends Controller{

    @FXML
    public JFXTextField txtUsuario;
    @FXML
    public JFXPasswordField txtContrasena;

    @Override
    public void initialize() {

    }
    public void iniciar(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        AuthenticationResponse login = LoginService.login(txtUsuario.getText().toString(), txtContrasena.getText().toString());

        if(login != null){
            FlowController.getInstance().goViewInNewStage("Principal",stage);
        }
    }

}
