package org.una.inventario.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.una.inventario.data.ActivoData;
import org.una.inventario.data.Reporte;
import org.una.inventario.util.AppContext;
import org.una.inventario.util.Mensaje;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusquedaCSVController extends Controller{
    @FXML
    public JFXButton btnBuscar;
    @FXML
    public Pane pPane;
    @FXML
    public ListView ListViewActivos;
    @FXML
    public TableView<ActivoData> tbActivos;
    @FXML
    public TableColumn clMarca;
    @FXML
    public TableColumn clProveedor;
    @FXML
    public TableColumn clNumero;
    @FXML
    public TableColumn clNota;
    @FXML
    public TableColumn clTelefono;
    @FXML
    public TableColumn tlCorreo;
    @FXML
    public TableColumn clProveedorFC;
    @FXML
    public TableColumn clContinente;
    @FXML
    public TableColumn clNombre;
    @FXML
    public TableColumn clEstado;
    @FXML
    public TableColumn clFechaCreacion;
    @FXML
    public JFXTextField txtMarca;
    @FXML
    public JFXTextField txtProveedor;
    @FXML
    public JFXTextField txtNumero;
    @FXML
    public TextArea taNota;
    @FXML
    public JFXTextField txtTelefono;
    @FXML
    public JFXTextField txtCorreo;
    @FXML
    public JFXTextField txtPFechaCreacion;
    @FXML
    public JFXTextField txtContinente;
    @FXML
    public JFXTextField txtNombre;
    @FXML
    public JFXTextField txtFechaCreacion;

    private Mensaje msg = new Mensaje();

    ObservableList<ActivoData> activos = FXCollections.observableArrayList();

    @Override
    public void initialize() {
        this.clMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        this.clProveedor.setCellValueFactory(new PropertyValueFactory("proveedor"));
        this.clNota.setCellValueFactory(new PropertyValueFactory("nota"));
        this.clNumero.setCellValueFactory(new PropertyValueFactory("numero"));
        this.clTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        this.tlCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        this.clProveedorFC.setCellValueFactory(new PropertyValueFactory("proveedorFechaCreacion"));
        this.clContinente.setCellValueFactory(new PropertyValueFactory("continente"));
        this.clNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        this.clEstado.setCellValueFactory(new PropertyValueFactory("estado"));
        this.clFechaCreacion.setCellValueFactory(new PropertyValueFactory("fechaCreacion"));
        setDataOnTableView();
        tbActivos.setEditable(true);
        this.clMarca.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clProveedor.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clNota.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clNumero.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clTelefono.setCellFactory(TextFieldTableCell.forTableColumn());
        this.tlCorreo.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clProveedorFC.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clContinente.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clEstado.setCellFactory(TextFieldTableCell.forTableColumn());
        this.clFechaCreacion.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void onActionBuscar(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        List<File> selectedFiles = (List<File>) fc.showOpenMultipleDialog(null);
        ObservableList<File> daFin = FXCollections.observableArrayList();

        String dErrores = "";
        if(selectedFiles != null){
            for(int i = 0; i < selectedFiles.size(); i++){
                if(validarArchivo(selectedFiles.get(i))){
                    ListViewActivos.getItems().add(selectedFiles.get(i).getName());
                    daFin.add(selectedFiles.get(i));
                }
                else{
                    dErrores += selectedFiles.get(i).getName()+ " ";
                    ListViewActivos.getItems().add(selectedFiles.get(i).getName());
                }
            }
        }


        if(dErrores != ""){
           //ListViewActivos
            msg.show(Alert.AlertType.INFORMATION, "Advertencia", "Los siguientes archivos presentan errores: "+dErrores);
        }

        final String errores = dErrores;
        ListViewActivos.setCellFactory(lv -> new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {

                        List<String> myList = new ArrayList<String>(List.of(errores.split(" ")));
                        for(int i = 0; i < myList.size(); i++){
                            if(Objects.equals(item, myList.get(i))){
                                this.setTextFill(Color.RED);
                            }
                        }
                        super.updateItem(item, empty);
                        setText(empty ? null : item);
                        //pseudoClassStateChanged(inactive, item != null && item.endsWith(" - not active"));
                    }
                });

        agregarTableView(daFin);
    }

    private boolean validarArchivo(File archivo){

        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(archivo.getAbsoluteFile()));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        String dato = records.get(0).get(0);
        int cantidad = 0;
        for(int i = 0; i < dato.length(); i++){
            if(dato.charAt(i)== ';'){
                cantidad++;
            }
        }

        records.clear();

        if(cantidad == 10){
            return true;
        }else{
            return false;
        }
    }

    private void agregarTableView(ObservableList<File> selectedFiles) {

        List<List<String>> records = new ArrayList<List<String>>();


        for(int i = 0; i < selectedFiles.size(); i++){
            try (CSVReader csvReader = new CSVReader(new FileReader(selectedFiles.get(i).getAbsoluteFile()));) {
                String[] values = null;
                while ((values = csvReader.readNext()) != null) {
                    records.add(Arrays.asList(values));
                    List<String> dat = Arrays.asList(values);
                    List<String> myList = new ArrayList<String>(Arrays.asList(Arrays.asList(values).get(0).split(";")));
                    if(!myList.get(1).equals("Proveedor")){
                        activos.add(new ActivoData(myList.get(0),myList.get(1),myList.get(2),myList.get(3),myList.get(4),myList.get(5),myList.get(6),myList.get(7),
                                myList.get(8),myList.get(9),myList.get(10)));
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }

        tbActivos.setItems(activos);
        AppContext.getInstance().set("activos",activos);

        for(int i = 0; i < activos.size(); i++){
            revisarCorreo(activos.get(i).getCorreo());
            validarFecha(activos.get(i).getFechaCreacion());
            isNumeric(activos.get(i).getNumero());
            validarNumeroTelefono(activos.get(i).getTelefono());
        }
    }

   private boolean revisarCorreo(String correo){
       // Patrón para validar el email
       // Patrón para validar el email
       Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

       // El email a validar
       String email = correo;

       Matcher mather = pattern.matcher(email);

       if (mather.find() == true) {
           System.out.println("El email ingresado es válido.");
           return true;
       } else {
           System.out.println("El email ingresado es inválido.");
           return false;
       }
   }

   private boolean validarFecha(String dato){
        boolean validar = false;
        try{
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dato);
            System.out.println("Fecha buena");
            return true;
        }catch (Exception e){
            System.out.println("Fecha mala");
            return false;
        }
   }

    private boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    private boolean validarNumeroTelefono(String numero){
        String regex = "\\d{4}-\\d{4}"; // XXX-XXX-XXX
        if(numero.matches(regex)){
            System.out.println("Numero bueno");
        }else{
            System.out.println("Numero malo");
        }
        return numero.matches(regex);
    }

    private void setDataOnTableView(){

        tbActivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //ActivoData act= activos.get(tbActivos.getSelectionModel().getSelectedIndex());
                //txtNombre.setText(act.getNombre());
            }
        });
    }

    public void onEditMarca(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setMarca((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setMarca((String) cellEditEvent.getNewValue());
    }

    public void onEditProovedor(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setProveedor((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setProveedor((String) cellEditEvent.getNewValue());
    }

    public void onEditNumero(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setNumero((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setNumero((String) cellEditEvent.getNewValue());
    }

    public void onEditNota(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setNota((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setNota((String) cellEditEvent.getNewValue());
    }

    public void onEditTelefono(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setTelefono((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setTelefono((String) cellEditEvent.getNewValue());
    }

    public void onEditCorreo(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setCorreo((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setCorreo((String) cellEditEvent.getNewValue());
    }

    public void onEditPFC(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setProveedorFechaCreacion((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setProveedorFechaCreacion((String) cellEditEvent.getNewValue());
    }

    public void onEditContinente(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setContinente((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setContinente((String) cellEditEvent.getNewValue());
    }

    public void onEditNombre(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setNombre((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setNombre((String) cellEditEvent.getNewValue());
    }

    public void onEditEstado(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setEstado((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setEstado((String) cellEditEvent.getNewValue());
    }

    public void onEditFechaCreacion(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setFechaCreacion((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setFechaCreacion((String) cellEditEvent.getNewValue());
    }
}
