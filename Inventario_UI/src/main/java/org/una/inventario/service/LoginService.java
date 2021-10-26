package org.una.inventario.service;

import org.una.inventario.dto.AuthenticationRequest;
import org.una.inventario.dto.AuthenticationResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LoginService {
    private static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/login";

    private static Mensaje msg = new Mensaje();

    public static AuthenticationResponse login(String cedula, String password) throws InterruptedException, ExecutionException, JsonProcessingException, IOException
    {
        AuthenticationRequest bean = new AuthenticationRequest();

        bean.setCedula(cedula);
        bean.setPassword(password);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

//        if(response.get().statusCode() == 500)
//            CargaGraficaMensaje( "Usuario no encontrado", root);
//        if (response.get().statusCode() == 401) {
//            CargaGraficaMensaje( "Contraseña incorrecta", root);
//        }
//        else
//        {
//            AuthenticationResponse authenticationResponse = JSONUtils.covertFromJsonToObject(response.get().body(), AuthenticationResponse.class);
//            return authenticationResponse;
//        }
        return null;
    }
}
