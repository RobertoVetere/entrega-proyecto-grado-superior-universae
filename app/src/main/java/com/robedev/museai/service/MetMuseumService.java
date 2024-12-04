package com.robedev.museai.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.robedev.museai.data.api.OkHttpService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MetMuseumService {

    // URL base de la API del Museo Met
    private static final String BASE_URL = "https://collectionapi.metmuseum.org/public/collection/v1/";
    private final MuseumApiImpl museumApi;
    private final OkHttpService okHttpService;

    private final Gson gson;

    public MetMuseumService() {
        this.museumApi = new MuseumApiImpl();
        this.gson = new Gson();
        this.okHttpService = new OkHttpService();
    }

    // Método para buscar por ID de objeto
    public String sendGetRequestById(String objectId) throws IOException {
        String endpoint = museumApi.getObjectById(objectId);
        return sendGetRequestToApi(endpoint);
    }

    // Método para buscar por una lista de IDs y devolver un array JSON
    public String sendGetRequestsByIds(List<String> objectIds) throws IOException {
        List<JsonElement> jsonResults = new ArrayList<>();

        for (String objectId : objectIds) {
            String jsonResponse = sendGetRequestById(objectId);

            // Parsear la respuesta JSON a un objeto JsonElement
            JsonElement jsonElement = JsonParser.parseString(jsonResponse);
            jsonResults.add(jsonElement);

            // Loguear el JSON obtenido para cada objectId
            Log.d("SendGetRequests", "JSON response for objectId " + objectId + ": " + jsonResponse);
        }

        // Loguear el contenido de la lista completa
        Log.d("SendGetRequests", "Final JSON results list: " + jsonResults);

        // Convertir la lista de JsonElements a un arreglo JSON
        return gson.toJson(jsonResults);
    }

    private String sendGetRequestToApi(String endpoint) throws IOException {
        String url = BASE_URL + endpoint;
        return okHttpService.sendGetRequest(url);
    }
}
