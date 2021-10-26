package org.una.inventario.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class PrincipalController extends Controller{

    @FXML
    public TableView tb_datos;
    @FXML
    public JFXButton btnGenerar;
    @FXML
    private TableColumn tb_id;
    @FXML
    private TableColumn tb_nombre;
    @FXML
    private TableColumn tb_fecha;
    @FXML
    public TableColumn tb_estado;
    @FXML
    private TableColumn tb_marca;

    @Override
    public void initialize() {

    }

    public void btnGenerar(ActionEvent actionEvent) {
    }
}
