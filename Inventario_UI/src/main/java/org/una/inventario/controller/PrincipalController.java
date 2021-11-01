package org.una.inventario.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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
import java.util.logging.Level;
import java.util.logging.Logger;


public class PrincipalController extends Controller{

    @FXML
    public Label txtUsuario;
    @FXML
    public JFXButton btnSalir;
    @FXML
    public BorderPane bpMenu;
    @FXML
    public Button btnCSV;
    @FXML
    public Button btnReportes;


    @Override
    public void initialize() {
       AuthenticationResponse rol = (AuthenticationResponse) AppContext.getInstance().get("Rol");
       txtUsuario.setText(rol.getUsuarioDTO().getNombreCompleto());
    }

    private void loadUI(String ui){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/org/una/inventario/view/"+ ui +".fxml"));
        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ((VBox) bpMenu.getCenter()).getChildren().clear();
        ((VBox) bpMenu.getCenter()).getChildren().add(root);

    }

<<<<<<< Updated upstream
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
=======
    public void ActionSalir(ActionEvent actionEvent) {
        AppContext.getInstance().delete("Rol");
        FlowController.getInstance().goViewInStage("Login",stage);
    }

    public void onActionCSV(ActionEvent actionEvent) {
        loadUI("BusquedaCSV");
>>>>>>> Stashed changes
    }

    public void onActionReportes(ActionEvent actionEvent) {
        loadUI("Reportes");
    }
}
