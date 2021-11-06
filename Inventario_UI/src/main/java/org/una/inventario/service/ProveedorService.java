package org.una.inventario.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import net.sf.jasperreports.phantomjs.ProcessOutputReader;
import org.una.inventario.dto.AuthenticationResponse;
import org.una.inventario.dto.MarcaDTO;
import org.una.inventario.dto.ProveedorDTO;
import org.una.inventario.util.AppContext;
import org.una.inventario.util.Mensaje;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ProveedorService {
    private static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8089/proveedores";
    private static ObjectMapper mapper = new ObjectMapper();
    private static Mensaje msg = new Mensaje();

    public static List<ProveedorDTO> getAllProveedores() throws InterruptedException, ExecutionException, IOException {

        List<ProveedorDTO> proveedores = null;
        AuthenticationResponse token = (AuthenticationResponse) AppContext.getInstance().get("Rol");

        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL))
                .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + token.getJwt()).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500){
            msg.show(Alert.AlertType.ERROR, "Error", "Ha ocurrido un error cargando los proovedores");
        }
        else if(response.get().statusCode() == 403){
            msg.show(Alert.AlertType.ERROR, "Error", "Se requiere un permiso adicional para realizar esta acción");
        }
        else
        {
            proveedores = mapper.readValue(response.get().body(), new TypeReference<List<ProveedorDTO>>() {});
            //List<HoraMarcajeDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HoraMarcajeDTO>>() {});
            //beans.forEach(System.out::println);
        }
        return proveedores;
    }

    public static void createProveedor(ProveedorDTO proveedor) throws InterruptedException, ExecutionException, JsonProcessingException {
        String convertJson = mapper.writeValueAsString(proveedor);
        AuthenticationResponse token = (AuthenticationResponse) AppContext.getInstance().get("Rol");
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + token.getJwt())
                .POST(HttpRequest.BodyPublishers.ofString(convertJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

        if(response.get().statusCode() == 500) {
            msg.show(Alert.AlertType.ERROR, "Error", "Sucedio un error creando el proveedor");
        }
        else if(response.get().statusCode() == 403){
            msg.show(Alert.AlertType.ERROR, "Error", "Se requiere un permiso adicional para realizar esta acción");
        }
        else {
            //TransaccionWebService.createTransaccion("Creación de Aerolinea.\nNombre: "+bean.getNombreAerolinea(),"Transacción",
            //FlowController.getInstance().authenticationResponse.getUsuario() , FlowController.getInstance().authenticationResponse.getJwt());
        }
        response.join();
    }
}
