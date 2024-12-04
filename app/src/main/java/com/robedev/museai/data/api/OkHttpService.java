package com.robedev.museai.data.api;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import java.io.IOException;

public class OkHttpService {

    private final OkHttpClient client;

    public OkHttpService() {
        this.client = new OkHttpClient();
    }

    // Método para realizar una petición GET
    public String sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la petición GET: " + response.code());
            }
            return response.body().string();
        }
    }

    // Método para realizar una petición POST
    public String sendPostRequest(String url, String jsonPayload) throws IOException {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonPayload, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Error en la petición POST: " + response.code());
            }
            return response.body().string();
        }
    }
}
