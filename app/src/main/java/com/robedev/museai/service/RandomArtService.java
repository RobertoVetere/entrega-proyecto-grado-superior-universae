package com.robedev.museai.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.utils.UtilsForJson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RandomArtService {

    private static final int MAX_ID = 471581;  // Rango máximo de IDs
    private static final int NUM_OBJECTS = 10; // Número de objetos a obtener
    private final MetMuseumService metMuseumService;

    private static final int PETICIONES_POR_SEGUNDO = 80;
    private Context context;
    public List<Artwork> artworks = new ArrayList<>();

    public RandomArtService() {
        this.metMuseumService = new MetMuseumService();
    }

    public List<Artwork> getArtworks() {
        return artworks;
    }

    public List<Artwork> obtenerArtworksPorIdsAleatorios() throws InterruptedException, IOException {
        // Generamos una lista de IDs aleatorios
        List<String> randomIds = generarIdsAleatorios(40); // Cambia la cantidad según sea necesario
        List<JSONObject> artworksJson = new ArrayList<>();  // Cambiado a List<JSONObject>

        // Creamos un ExecutorService con un número adecuado de hilos
        ExecutorService executor = Executors.newFixedThreadPool(10); // Puedes ajustar el número de hilos según la capacidad
        Semaphore semaphore = new Semaphore(PETICIONES_POR_SEGUNDO); // Para controlar el límite de 80 peticiones por segundo

        for (String id : randomIds) {
            executor.submit(() -> {
                try {
                    // Espera hasta que haya permisos disponibles para hacer la solicitud
                    semaphore.acquire();

                    // Hacemos la petición al servicio MetMuseumService
                    String response = metMuseumService.sendGetRequestById(id);

                    if (response != null && !response.isEmpty()) {
                        JsonObject artwork = JsonParser.parseString(response).getAsJsonObject();

                        // Convertimos el JsonObject a JSONObject y lo agregamos a la lista
                        JSONObject jsonObject = new JSONObject(artwork.toString());
                        synchronized (artworksJson) {
                            artworksJson.add(jsonObject);
                        }

                        // Imprimir el JSON completo en la consola (log)
                        Log.i("RandomArtService", "Obtenido artwork para ID: " + id);
                        Log.i("RandomArtService", artwork.toString());  // Imprime el objeto JSON completo
                    } else {
                        Log.e("RandomArtService", "Respuesta nula o vacía para el ID: " + id);
                    }

                } catch (Exception e) {
                    Log.e("RandomArtService", "Error al obtener el ID: " + id + " - " + e.getMessage());
                } finally {
                    // Libera el semáforo para permitir que otro hilo haga una solicitud
                    semaphore.release();
                }
            });
        }

        // Cerramos el ExecutorService después de que se complete todo el trabajo
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Convertimos los objetos JSON obtenidos a una lista de Artwork usando el método de UtilsForJson
        artworks = UtilsForJson.convertToArtworks(artworksJson);

        // Retornamos la lista de artworks
        return artworks;
    }
    private List<String> generarIdsAleatorios(int cantidad) {
        List<String> idsAleatorios = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < cantidad; i++) {
            int randomId = random.nextInt(MAX_ID) + 1; // Genera un número aleatorio entre 1 y MAX_ID
            idsAleatorios.add(String.valueOf(randomId));
        }

        return idsAleatorios;
    }





}
