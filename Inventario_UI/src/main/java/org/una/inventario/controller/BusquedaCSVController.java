package org.una.inventario.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.util.Callback;
import javafx.util.Duration;
import lombok.val;
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
import java.util.concurrent.TimeUnit;
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
    @FXML
    public JFXButton btnCancelar;
    @FXML
    public JFXButton btnSiguenteDescripcion;
    @FXML
    public JFXButton btnCancelarSeleccionar;
    @FXML
    public JFXButton btnSiguienteSeleccionar;
    @FXML
    public JFXButton btnCancelarSubir;
    @FXML
    public JFXButton btnSiguienteSubir;
    @FXML
    public HBox vbEditarDatos;
    @FXML
    public Text txtDatosTotal;
    @FXML
    public Text txtSubirDatos;
    @FXML
    public ImageView imgSpinner;
    @FXML
    public Text txtDatosSubidos;
    @FXML
    public Text txtInformacion;
    @FXML
    public ImageView img60;
    @FXML
    public ImageView img90;
    @FXML
    public ImageView img100;

    private Mensaje msg = new Mensaje();

    private int j = 0;

    private String errores = "";

    private String modificados = "";

    private String descartados = "";

    private long subidaDB = 0;

    private long regInit = 0;

    private long regFin = 0;

    private ObservableList<ActivoData> activos = FXCollections.observableArrayList();

    private ObservableList<Integer> lineas = FXCollections.observableArrayList();

    private ObservableList<String> invRep = FXCollections.observableArrayList();

    private ObservableList<String> mrcRep = FXCollections.observableArrayList();

    private SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void initialize() {

        regInit = System.nanoTime();

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
        setDataOnTableView();
    }

    public void onActionBuscar(ActionEvent actionEvent) {

        Date fcDate = new Date();
        String fecha = fcDate.toString();
        errores += fecha+"\n";
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        List<File> selectedFiles = (List<File>) fc.showOpenMultipleDialog(null);
        ObservableList<File> daFin = FXCollections.observableArrayList();

        errores += "\nArchivos con cargados: ";

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
                errores += selectedFiles.get(i).getName()+ " ";
            }
        }

        errores += "\nArchivos con errores: "+ dErrores;

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


        int cantidad = 0;
        try{
            if(records.get(0).size() != 0){
                String dato = records.get(0).get(0);
                for(int i = 0; i < dato.length(); i++){
                    if(dato.charAt(i)== ';'){
                        cantidad++;
                    }
                }
            }
        }catch (Exception e){

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

            File file = new File(ruta);

            errores += "\nLineas con errores: ";

            for(int i = 0; i < tbActivos.getItems().size(); i++){
                if(!validarNumeroTelefono(tbActivos.getItems().get(i).getTelefono()) || !validarFecha(tbActivos.getItems().get(i).getFechaCreacion()) || !isString(tbActivos.getItems().get(i).getNota())){
                    errores += i+1 + " ";
                }
            }

            errores += "\nLineas modificadas: ";
            errores += modificados;

            errores += "\nLineas descartadas: ";
            errores += descartados;

            errores += "\nTiempo edicion de registros: ";
            regFin = System.nanoTime();
            Long fin = regFin - regInit;
            long convertS1 = TimeUnit.SECONDS.convert(fin, TimeUnit.NANOSECONDS);
            errores += convertSecondstoMinutes(Integer.parseInt(String.valueOf(convertS1)));

            errores += "\nTiempo de subida en BD: ";
            long convert2 = TimeUnit.SECONDS.convert(subidaDB, TimeUnit.NANOSECONDS);
            errores += convertSecondstoMinutes(Integer.parseInt(String.valueOf(convert2)));

            if(!file.exists()){
                file.createNewFile();
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String linea;
            String datos = "";
            while((linea = bufferedReader.readLine())!=null) {
                datos += linea+"\n";
            }
            bufferedReader.close();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(datos+"\n\n"+errores);
            bufferedWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

   private boolean revisarCorreo(String correo){
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

    private String convertSecondstoMinutes(int secods){
        int input= secods;
        int hours=0;
        int minutes=0;
        int seconds=0;
        //Hour Conversion
        if((input/3600)!=0) // 1 hour = 3600 seconds
        {
            hours=input/3600;
            input=input%3600;
        }
        //Minute Conversion
        if((input/60)!=0) //1 minute= 60 Seconds
        {
            minutes=input/60;
            input=input%60;
        }
        //Second Conversion
        if((input)!=0)
        {
            seconds=input;
        }
        String output=hours+" horas : "+minutes+" minutos :"+seconds+" Segudos ";
        return output;
    }
    private void setDataOnTableView(){

        tbActivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(tbActivos.getSelectionModel().getSelectedIndex() >= 0 && tbActivos.getSelectionModel().getSelectedIndex() < activos.size()){
                    ActivoData act = tbActivos.getItems().get(tbActivos.getSelectionModel().getSelectedIndex());
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

        modificados += String.valueOf(index)+ " ";

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
        descartados += String.valueOf(tbActivos.getSelectionModel().getSelectedIndex())+" ";
    }

    public void onEditMarca(TableColumn.CellEditEvent cellEditEvent) {
        ActivoData activoData = tbActivos.getSelectionModel().getSelectedItem();
        System.out.println("Campo: "+ tbActivos.getSelectionModel().getSelectedIndex());
        activoData.setMarca((String) cellEditEvent.getNewValue());
        activos.get(tbActivos.getSelectionModel().getSelectedIndex()).setMarca((String) cellEditEvent.getNewValue());
    }

    public void onActionSubir(ActionEvent actionEvent) throws IOException, ExecutionException, InterruptedException, ParseException {

        imgSpinner.setVisible(true);
        TranslateTransition translate = new TranslateTransition();
        translate.setDuration(Duration.millis(3000));
        translate.setAutoReverse(true);
        translate.setNode(imgSpinner);
        translate.play();

        /*
         Platform.runLater(new Runnable() {
            @Override public void run() {

            }
        });
         */

        Long t = System.nanoTime();

        Thread thread = new Thread(){
            public void run(){
                List<MarcaDTO> marcaDB = null;
                try {
                    marcaDB = MarcaService.getAllMarcas();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                List<ProveedorDTO> proveedorDB = null;
                try {
                    proveedorDB = ProveedorService.getAllProveedores();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < marcaDB.size(); i++){
                    mrcRep.add(marcaDB.get(i).getNombre());
                }

                for(int i = 0; i < proveedorDB.size(); i++){
                    invRep.add(proveedorDB.get(i).getNombre());
                }

                for(int i = 0; i < tbActivos.getItems().size(); i++) {
                    if(tbActivos.getItems().get(i).getMarca() != "" && tbActivos.getItems().get(i).getMarca() != null && tbActivos.getItems().get(i).getMarca() != " ") {
                        if (!analizarRepetido(tbActivos.getItems().get(i).getMarca(),mrcRep)) {
                            MarcaDTO nm = new MarcaDTO();
                            nm.setEstado(true);
                            nm.setNombre(tbActivos.getItems().get(i).getMarca());
                            try {
                                if(validarFecha(tbActivos.getItems().get(i).getFechaCreacion())){
                                    nm.setFechaCreacion(converDate(tbActivos.getItems().get(i).getFechaCreacion()));
                                }else{
                                    nm.setFechaCreacion(null);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            try {
                                MarcaService.createMarca(nm);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if(tbActivos.getItems().get(i).getProveedor() != "" && tbActivos.getItems().get(i).getProveedor() != null && tbActivos.getItems().get(i).getProveedor() != " ") {
                        if (!analizarRepetido(tbActivos.getItems().get(i).getProveedor(),invRep)) {
                            ProveedorDTO np = new ProveedorDTO();
                            np.setCorreo(tbActivos.getItems().get(i).getCorreo());
                            np.setEstado(true);
                            try {
                                if(validarFecha(tbActivos.getItems().get(i).getFechaCreacion())){
                                    np.setFechaCreacion(converDate(tbActivos.getItems().get(i).getFechaCreacion()));
                                }
                                else{
                                    np.setFechaCreacion(null);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            np.setNombre(tbActivos.getItems().get(i).getProveedor());
                            if(!isString(tbActivos.getItems().get(i).getNota())){
                                np.setNotas(tbActivos.getItems().get(i).getNota());
                            }
                            else{
                                np.setNotas("");
                            }
                            np.setTelefono(tbActivos.getItems().get(i).getTelefono());
                            try {
                                ProveedorService.createProveedor(np);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                try {
                    marcaDB = MarcaService.getAllMarcas();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    proveedorDB = ProveedorService.getAllProveedores();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for(int i = 0; i < tbActivos.getItems().size(); i++){
                    ActivoDTO newActivo = new ActivoDTO();
                    if(!Objects.equals(tbActivos.getItems().get(i).getContinente(), "")){
                        newActivo.setContinente(Long.parseLong(tbActivos.getItems().get(i).getContinente()));
                    }
                    boolean est = false;
                    if(Objects.equals(tbActivos.getItems().get(i).getEstado(), "activo")){
                        est = true;
                    }else{
                        est = false;
                    }
                    newActivo.setEstado(est);
                    try {
                        if(validarFecha(tbActivos.getItems().get(i).getFechaCreacion())){
                            newActivo.setFechaCreacion(converDate(tbActivos.getItems().get(i).getFechaCreacion()));
                        }else{
                            newActivo.setFechaCreacion(null);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    newActivo.setMarca(getMarca(marcaDB,tbActivos.getItems().get(i).getMarca()));
                    newActivo.setProveedor(getProveedor(proveedorDB,tbActivos.getItems().get(i).getProveedor()));
                    newActivo.setNombre(tbActivos.getItems().get(i).getNombre());
                    try {
                        ActivoService.createActivo(newActivo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                
                Long f = System.nanoTime();
                subidaDB = f-t;
                archivo();
            }

        };
        thread.start();

        translate.setOnFinished(event -> {
            img90.setVisible(false);
            img100.setVisible(true);
            txtDatosTotal.setVisible(false);
            txtSubirDatos.setVisible(false);
            txtDatosSubidos.setVisible(true);
            btnCancelarSubir.setVisible(false);
            btnSubir.setVisible(false);
            btnSiguienteSubir.setText("Finalizar");

            imgSpinner.setVisible(false);

        });
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
        this.stage.close();
    }

    public void onActionSiguienteDescripcion(ActionEvent actionEvent) {

      pane_descripcion.setVisible(false);
      PaneSeleccionar.setVisible(true);

    }

    public void onActionCancelarSeleccionar(ActionEvent actionEvent) {
        PaneSeleccionar.setVisible(false);
        pane_descripcion.setVisible(true);
        this.stage.close();
    }

    public void onActionSiguienteSelecccionar(ActionEvent actionEvent) {
        PaneSeleccionar.setVisible(false);
        paneEditar.setVisible(true);
    }

    public void onActionCancelarSubir(ActionEvent actionEvent) {
        paneEditar.setVisible(false);
        PaneSeleccionar.setVisible(false);
        pane_descripcion.setVisible(true);
        this.stage.close();
    }

    public void onActionSiguienteSubir(ActionEvent actionEvent) {

        if(btnSiguienteSubir.getText().equals("Finalizar")){
            btnSiguienteSubir.setText("Siguiente");
            txtDatosSubidos.setVisible(false);
            btnCancelarSubir.setVisible(true);
            btnSubir.setVisible(false);
            vbEditarDatos.setVisible(true);
            this.stage.close();
        }
        else{
            img60.setVisible(false);
            img90.setVisible(true);
            vbEditarDatos.setVisible(false);
            btnDescargar.setVisible(false);
            btnModificar.setVisible(false);
            txtDatosTotal.setVisible(true);
            txtInformacion.setVisible(false);
            txtDatosTotal.setText("Se subiran un total de "+tbActivos.getItems().size()+" activos a la base de datos");
            txtSubirDatos.setVisible(true);
            btnSubir.setVisible(true);
            btnSiguienteSubir.setText("Finalizar");
        }
    }

}
