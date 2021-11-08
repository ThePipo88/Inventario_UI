package org.una.inventario.data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.*;
import net.sf.jasperreports.view.*;
import org.una.inventario.dto.AuthenticationResponse;
import org.una.inventario.dto.RolDTO;
import org.una.inventario.util.AppContext;

public class ReporteDataSource implements JRDataSource{


    private ObservableList<Reporte> reportes = FXCollections.observableArrayList();
    private int index;
    public ReporteDataSource(){
        reportes = (ObservableList<Reporte>) AppContext.getInstance().get("reporte");
    }

    @Override
    public boolean next() throws JRException {
        index++;
        return (index < reportes.size());
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {

        Object value = null;

        String fieldName = jrField.getName();

        switch(fieldName){

            case "id":
                value = reportes.get(index).getId();
                break;

            case "nombre":
                value = reportes.get(index).getNombre();
                break;

            case "fecha":
                value = reportes.get(index).getFecha();
                break;

            case "estado":
                value = reportes.get(index).getEstado();
                break;

            case "marca":
                value = reportes.get(index).getMarca();
                break;
            case "total":
                value = String.valueOf(reportes.size());
                break;
            case "auditor":
                AuthenticationResponse auditor = (AuthenticationResponse) AppContext.getInstance().get("Rol");
                value = auditor.getUsuarioDTO().getNombreCompleto();
                break;
        }

        return value;
    }

    public static JRDataSource getDataSource(){
        return new ReporteDataSource();
    }
}
