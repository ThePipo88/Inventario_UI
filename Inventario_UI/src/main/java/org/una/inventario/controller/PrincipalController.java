package org.una.inventario.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.una.inventario.data.Reporte;
import org.una.inventario.dto.AuthenticationResponse;
import org.una.inventario.util.AppContext;


public class PrincipalController extends Controller{

    @FXML
    public TableView<Reporte> tb_datos;
    @FXML
    public JFXButton btnGenerar;
    @FXML
    public Label txtUsuario;
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

    private ObservableList<Reporte> reportes = FXCollections.observableArrayList();

    @Override
    public void initialize() {
        AuthenticationResponse authenticationResponse = (AuthenticationResponse) AppContext.getInstance().get("Rol");
        txtUsuario.setText(authenticationResponse.getUsuarioDTO().getNombreCompleto());

        reportes.add(new Reporte("1","Desinfectante","2021-01-01","Activo","Suavitel"));
        reportes.add(new Reporte("2","Lo que sea","2021-01-01","Activo","Suavitel"));
        reportes.add(new Reporte("3","Lo que seargfrbt","2021-01-01","Activo","Suartvrtvitel"));
        reportes.add(new Reporte("4","Lo qurtvrtve sea","2021-01-01","Activortv","Suavitrtvrtvel"));
        inicializarTabla();
    }

    public void btnGenerar(ActionEvent actionEvent) {
    }

    private void inicializarTabla(){
        this.tb_id.setCellValueFactory(new PropertyValueFactory("id"));
        this.tb_nombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.tb_fecha.setCellValueFactory(new PropertyValueFactory("fecha"));
        this.tb_estado.setCellValueFactory(new PropertyValueFactory("estado"));
        this.tb_marca.setCellValueFactory(new PropertyValueFactory("marca"));
        this.tb_datos.setItems(reportes);
    }
}
