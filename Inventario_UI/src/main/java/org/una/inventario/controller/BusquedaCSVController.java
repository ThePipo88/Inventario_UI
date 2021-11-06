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
import javafx.util.Callback;
import org.una.inventario.data.ActivoData;
import org.una.inventario.data.Reporte;
import org.una.inventario.dto.ActivoDTO;
import org.una.inventario.dto.MarcaDTO;
import org.una.inventario.dto.ProveedorDTO;
import org.una.inventario.dto.RolDTO;
import org.una.inventario.service.ActivoService;
import org.una.inventario.service.MarcaService;
import org.una.inventario.service.ProveedorService;
import org.una.inventario.util.AppContext;
import org.una.inventario.util.FlowController;
import org.una.inventario.util.Mensaje;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BusquedaCSVController extends Controller{
    @FXML
    public JFXButton btnBuscar;
    @FXML
    public Pane pPane;
    @FXML
    public Pane pane_descripcion;
    @FXML
    public Pane PaneSeleccionar ;
    @FXML
    public Pane paneEditar ;
    @FXML
    public ListView ListViewActivos;
    @FXML
    public TableView<ActivoData> tbActivos;
    @FXML
    public TableColumn<ActivoData,String> clMarca;
    @FXML
    public TableColumn<ActivoData,String> clProveedor;
    @FXML
    public TableColumn<ActivoData,String> clNumero;
    @FXML
    public TableColumn<ActivoData,String> clNota;
    @FXML
    public TableColumn<ActivoData,String> clTelefono;
    @FXML
    public TableColumn<ActivoData,String> tlCorreo;
    @FXML
    public TableColumn<ActivoData,String> clProveedorFC;
    @FXML
    public TableColumn<ActivoData,String> clContinente;
    @FXML
    public TableColumn<ActivoData,String> clNombre;
    @FXML
    public TableColumn<ActivoData,String> clEstado;
    @FXML
    public TableColumn<ActivoData,String> clFechaCreacion;
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
    public JFXTextField txtPFC;
    @FXML
    public JFXTextField txtContinente;
    @FXML
    public JFXTextField txtNombre;
    @FXML
    public JFXTextField txtEstado;
    @FXML
    public JFXTextField txtFechaCreacion;
    @FXML
    public JFXButton btnModificar;
    @FXML
    public JFXButton btnDescargar;
    @FXML
    public JFXButton btnSubir;

    private Mensaje msg = new Mensaje();

    private int j = 0;

    private int lineError = 1;

    private ObservableList<ActivoData> activos = FXCollections.observableArrayList();

    private ObservableList<Integer> lineas = FXCollections.observableArrayList();

    private ObservableList<String> invRep = FXCollections.observableArrayList();

    private ObservableList<String> mrcRep = FXCollections.observableArrayList();

    private SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void initialize() {


        String ventana = (String) AppContext.getInstance().get("Ventana");

        this.clMarca.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("marca"));
        this.clProveedor.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("proveedor"));
        this.clNota.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("nota"));
        this.clNumero.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("numero"));
        this.clTelefono.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("telefono"));
        this.tlCorreo.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("correo"));
        this.clProveedorFC.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("proveedorFechaCreacion"));
        this.clContinente.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("continente"));
        this.clNombre.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("nombre"));
        this.clEstado.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("estado"));
        this.clFechaCreacion.setCellValueFactory(new PropertyValueFactory<ActivoData,String>("fechaCreacion"));

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

        setDataOnTableView();
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
        revisarCampos(clTelefono,1);
        revisarCampos(clProveedorFC,2);
        revisarCampos(clFechaCreacion,2);
        revisarCampos(clNota,3);

        archivo();
    }

    private void revisarCampos(TableColumn<ActivoData,String> columna, int tipo){

        columna.setCellFactory(column -> new TableCell<ActivoData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                this.setStyle("-fx-text-fill: white;");
                if(item != null){
                    if(tipo == 1){
                        if(!validarNumeroTelefono(item)){
                            this.setStyle("-fx-text-fill: red;");
                        }
                    }
                    else if(tipo == 2){
                        if(!validarFecha(item)){
                            this.setStyle("-fx-text-fill: red;");
                        }
                    }
                    else if(tipo == 3){
                        if(!isString(item)){
                            this.setStyle("-fx-text-fill: red;");
                        }
                    }
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            }
        });
    }

    public void archivo(){
        try{
            String ruta = new File("ReporteErrores.txt").getAbsolutePath();
            String contenido = "";
            String msg = "Lineas con Errores: ";

            File file = new File(ruta);

            for(int i = 0; i < activos.size(); i++){
                if(!validarNumeroTelefono(activos.get(i).getTelefono()) || !validarFecha(activos.get(i).getFechaCreacion()) || !isString(activos.get(i).getNota())){
                    contenido += i+1 + " ";
                }
            }

            if(!file.exists()){
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(msg);
            bufferedWriter.write(contenido);
            bufferedWriter.close();
        }catch (Exception e){
            e.printStackTrace();
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
           return true;
       } else {
           return false;
       }
   }

   private boolean validarFecha(String dato){
        boolean validar = false;
        try{
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dato);
            return true;
        }catch (Exception e){
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

    private boolean isString(String cadena){
        try {
            Integer.parseInt(cadena);
            return false;
        } catch (NumberFormatException nfe){
            return true;
        }
    }

    private boolean validarNumeroTelefono(String numero){
        String regex = "\\d{4}-\\d{4}"; // XXX-XXX-XXX
        return numero.matches(regex);
    }

    private void setDataOnTableView(){

        tbActivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(tbActivos.getSelectionModel().getSelectedIndex() >= 0 && tbActivos.getSelectionModel().getSelectedIndex() < activos.size()){
                    ActivoData act= activos.get(tbActivos.getSelectionModel().getSelectedIndex());
                    txtNombre.setText(act.getNombre());
                    txtEstado.setText(act.getEstado());
                    txtContinente.setText(act.getContinente());
                    txtCorreo.setText(act.getCorreo());
                    txtPFC.setText(act.getProveedorFechaCreacion());
                    txtFechaCreacion.setText(act.getFechaCreacion());
                    txtNumero.setText(act.getNumero());
                    txtMarca.setText(act.getMarca());
                    txtProveedor.setText(act.getProveedor());
                    txtTelefono.setText(act.getTelefono());
                    taNota.setText(act.getNota());
                }
            }
        });
    }

    public void onActionModificar(ActionEvent actionEvent) {
        int index = tbActivos.getSelectionModel().getSelectedIndex();

        activos.get(index).setMarca(txtMarca.getText());
        activos.get(index).setFechaCreacion(txtFechaCreacion.getText());
        activos.get(index).setEstado(txtEstado.getText());
        activos.get(index).setNombre(txtNombre.getText());
        activos.get(index).setContinente(txtContinente.getText());
        activos.get(index).setCorreo(txtCorreo.getText());
        activos.get(index).setProveedorFechaCreacion(txtPFC.getText());
        activos.get(index).setNota(taNota.getText());
        activos.get(index).setTelefono(txtTelefono.getText());
        activos.get(index).setNumero(txtNumero.getText());
        activos.get(index).setProveedor(txtProveedor.getText());

        editarTexto(clMarca,txtMarca.getText());
        editarTexto(clFechaCreacion,txtFechaCreacion.getText());
        editarTexto(clEstado,txtEstado.getText());
        editarTexto(clNombre,txtNombre.getText());
        editarTexto(clContinente,txtContinente.getText());
        editarTexto(tlCorreo,txtCorreo.getText());
        editarTexto(clProveedorFC,txtPFC.getText());
        editarTexto(clNota,taNota.getText());
        editarTexto(clTelefono,txtTelefono.getText());
        editarTexto(clNumero,txtNumero.getText());
        editarTexto(clProveedor,txtProveedor.getText());

        revisarCampos(clTelefono,1);
        revisarCampos(clProveedorFC,2);
        revisarCampos(clFechaCreacion,2);
        revisarCampos(clNota,3);
    }

    private void editarTexto(TableColumn<ActivoData,String> columna, String texto){

        columna.setCellFactory(column -> new TableCell<ActivoData, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                if(item != null){
                    if(j == tbActivos.getSelectionModel().getSelectedIndex()){
                        setText(texto);
                    }
                    j++;
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            }
        });
    }

    public void onActioDescartar(ActionEvent actionEvent) {
        tbActivos.getItems().remove(tbActivos.getSelectionModel().getSelectedIndex());
        activos.remove(tbActivos.getSelectionModel().getSelectedIndex());
    }

    public void onEditMarca(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setMarca((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setMarca((String) cellEditEvent.getNewValue());
    }

    public void onActionSubir(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException, ParseException {

        List<MarcaDTO> marcaDB = MarcaService.getAllMarcas();
        List<ProveedorDTO> proveedorDB = ProveedorService.getAllProveedores();

        for(int i = 0; i < marcaDB.size(); i++){
            mrcRep.add(marcaDB.get(i).getNombre());
        }

        for(int i = 0; i < proveedorDB.size(); i++){
            invRep.add(proveedorDB.get(i).getNombre());
        }

        for(int i = 0; i < activos.size(); i++) {
            if(activos.get(i).getMarca() != "" && activos.get(i).getMarca() != null && activos.get(i).getMarca() != " ") {
                if (!analizarRepetido(activos.get(i).getMarca(),mrcRep)) {
                    MarcaDTO nm = new MarcaDTO();
                    nm.setEstado(true);
                    nm.setNombre(activos.get(i).getMarca());
                    nm.setFechaCreacion(converDate(activos.get(i).getFechaCreacion()));
                    MarcaService.createMarca(nm);
                }
            }

            if(activos.get(i).getProveedor() != "" && activos.get(i).getProveedor() != null && activos.get(i).getProveedor() != " ") {
                if (!analizarRepetido(activos.get(i).getProveedor(),invRep)) {
                    ProveedorDTO np = new ProveedorDTO();
                    np.setCorreo(activos.get(i).getCorreo());
                    np.setEstado(true);
                    np.setFechaCreacion(converDate(activos.get(i).getFechaCreacion()));
                    np.setNombre(activos.get(i).getProveedor());
                    np.setNotas(activos.get(i).getNota());
                    np.setTelefono(activos.get(i).getTelefono());
                    ProveedorService.createProveedor(np);
                }
            }
        }

        marcaDB = MarcaService.getAllMarcas();
        proveedorDB = ProveedorService.getAllProveedores();

        for(int i = 0; i < activos.size(); i++){
            ActivoDTO newActivo = new ActivoDTO();
            if(!Objects.equals(activos.get(i).getContinente(), "")){
                newActivo.setContinente(Long.parseLong(activos.get(i).getContinente()));
            }
            boolean est = false;
            if(Objects.equals(activos.get(i).getEstado(), "activo")){
                est = true;
            }else{
                est = false;
            }
            newActivo.setEstado(est);
            newActivo.setFechaCreacion(converDate(activos.get(i).getFechaCreacion()));
            newActivo.setMarca(getMarca(marcaDB,activos.get(i).getMarca()));
            newActivo.setProveedor(getProveedor(proveedorDB,activos.get(i).getProveedor()));
            newActivo.setNombre(activos.get(i).getNombre());
            ActivoService.createActivo(newActivo);
        }

    }

    private boolean analizarRepetido(String dato, ObservableList<String> lista){
        if(lista != null){
            for(int i = 0; i < lista.size(); i++){
                if(Objects.equals(lista.get(i), dato)){
                    return true;
                }
            }
            lista.add(dato);
            return false;
        }
        else{
            lista.add(dato);
            return false;
        }
    }

    private Date converDate(String date) throws ParseException {
        Date nd = new Date();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(date, formato);
        nd = form.parse(fecha.toString());
        return nd;
    }
    private MarcaDTO getMarca(List<MarcaDTO> listMarca, String nombre){
        for(int i = 0; i < listMarca.size(); i++){
            if(listMarca.get(i).getNombre().equals(nombre)){
                return listMarca.get(i);
            }
        }
        return null;
    }

    private ProveedorDTO getProveedor(List<ProveedorDTO> listProveedor, String nombre){
        for(int i = 0; i < listProveedor.size(); i++){
            if(listProveedor.get(i).getNombre().equals(nombre)){
                return listProveedor.get(i);
            }
        }
        return null;
    }
    public void onActionCancelarDescripcion(ActionEvent actionEvent) {

        pane_descripcion.setVisible(false);
        PaneSeleccionar.setVisible(false);


    }

    public void onActionSiguienteDescripcion(ActionEvent actionEvent) {

      pane_descripcion.setVisible(false);
      PaneSeleccionar.setVisible(true);

    }

    public void onActionCancelarSeleccionar(ActionEvent actionEvent) {
        PaneSeleccionar.setVisible(false);
        pane_descripcion.setVisible(false);
    }

    public void onActionSiguienteSelecccionar(ActionEvent actionEvent) {
    }

    public void onActionCancelarSubir(ActionEvent actionEvent) {
    }

    public void onActionSiguienteSubir(ActionEvent actionEvent) {
    }

}
