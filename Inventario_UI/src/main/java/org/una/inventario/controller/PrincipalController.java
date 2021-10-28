package org.una.inventario.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import javax.swing.WindowConstants;
import org.una.inventario.data.Reporte;
import org.una.inventario.data.ReporteDataSource;
import org.una.inventario.dto.ActivoDTO;
import org.una.inventario.dto.AuthenticationResponse;
import org.una.inventario.service.ActivoService;
import org.una.inventario.util.AppContext;
import org.una.inventario.util.FlowController;
import org.una.inventario.util.Mensaje;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PrincipalController extends Controller{

    @FXML
    public TableView<Reporte> tb_datos;
    @FXML
    public JFXButton btnGenerar;
    @FXML
    public Label txtUsuario;
    @FXML
    public JFXButton btnProveedor;
    @FXML
    public DatePicker dateInicial;
    @FXML
    public DatePicker dateFinal;
    @FXML
    public JFXButton btnMarca;
    @FXML
    public Label txtAgrupacion;
    @FXML
    public JFXButton btnSalir;
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

    private static Mensaje msg = new Mensaje();

    private Object [][] listadoPaises = null;

    @Override
    public void initialize() {
        AuthenticationResponse authenticationResponse = (AuthenticationResponse) AppContext.getInstance().get("Rol");
        txtUsuario.setText(authenticationResponse.getUsuarioDTO().getNombreCompleto());
        inicializarTabla();
    }

    public void btnGenerar(ActionEvent actionEvent) {
        try{

            JasperReport report = (JasperReport) JRLoader.loadObject(getClass().getResource("/org/una/inventario/reporteJasper/Datos.jasper"));
            JasperPrint jprint = JasperFillManager.fillReport(report, null, ReporteDataSource.getDataSource());

            JasperViewer view = new JasperViewer(jprint, false);
            view.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            view.setVisible(true);


        }catch(JRException ex){
            ex.getMessage();
        }
    }

    private void inicializarTabla(){
        this.tb_id.setCellValueFactory(new PropertyValueFactory("id"));
        this.tb_nombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.tb_fecha.setCellValueFactory(new PropertyValueFactory("fecha"));
        this.tb_estado.setCellValueFactory(new PropertyValueFactory("estado"));
        this.tb_marca.setCellValueFactory(new PropertyValueFactory("marca"));
        reportes.add(new Reporte("","","","",""));
        this.tb_datos.setItems(reportes);
    }

    public void ActionProveedor(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {
        LocalDate inicio = dateInicial.getValue();
        LocalDate fin = dateFinal.getValue();

        if(inicio != null && fin != null){
            txtAgrupacion.setText("Proveedor");
            this.tb_datos.getColumns().get(4).setText("Proveedor");
            reportes.clear();

            List<ActivoDTO> activos = ActivoService.getActivo(inicio.toString(),fin.toString());

            if(activos != null){

                for(ActivoDTO activo:activos) {
                    reportes.add(new Reporte(activo.getId().toString(),activo.getNombre(),activo.getFechaCreacion().toString(),"1",activo.getProveedor().getNombre()));
                }

                Collections.sort(reportes);
                AppContext.getInstance().delete("reporte");
                AppContext.getInstance().set("reporte",reportes);

                this.tb_datos.setItems(reportes);
            }

        }
        else{
            msg.show(Alert.AlertType.ERROR, "Error", "La fechas ingresadas son incorrectas, vuelva a intentarlo");
        }
    }

    public void ActionMarca(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException {

        LocalDate inicio = dateInicial.getValue();
        LocalDate fin = dateFinal.getValue();

        if(inicio != null && fin != null){
            txtAgrupacion.setText("Marca");
            this.tb_datos.getColumns().get(4).setText("Marca");
            reportes.clear();

            List<ActivoDTO> activos = ActivoService.getActivo(inicio.toString(),fin.toString());

            if(activos != null){
                for(ActivoDTO activo:activos) {
                    reportes.add(new Reporte(activo.getId().toString(),activo.getNombre(),activo.getFechaCreacion().toString(),"1",activo.getMarca().getNombre()));
                }

                Collections.sort(reportes);
                AppContext.getInstance().delete("reporte");
                AppContext.getInstance().set("reporte",reportes);

                this.tb_datos.setItems(reportes);
            }

        }else{
            msg.show(Alert.AlertType.ERROR, "Error", "La fechas ingresadas son incorrectas, vuelva a intentarlo");
        }
    }

    public void ActionSalir(ActionEvent actionEvent) {
        AppContext.getInstance().delete("Rol");
        FlowController.getInstance().goViewInStage("Login",stage);
    }
}
