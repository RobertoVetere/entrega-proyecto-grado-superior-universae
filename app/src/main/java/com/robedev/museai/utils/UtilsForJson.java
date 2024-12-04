package com.robedev.museai.utils;

import android.content.Context;
import android.util.Log;

import com.robedev.museai.data.model.Artwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UtilsForJson {

    public static String loadJSONFromAsset(Context context, String filename) {
        Log.i("UtilsForJson", "archivo: " + filename);
        StringBuilder jsonBuilder = new StringBuilder();
        try (InputStream inputStream = context.getAssets().open(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            Log.i("UtilsForJson", "Archivo cargado correctamente: " + filename);
        } catch (IOException e) {
            Log.e("UtilsForJson", "Error loading JSON file: " + filename, e);
        }
        return jsonBuilder.toString();
    }

    public static List<Artwork> getArtworksFromJson(String jsonResult, String artistName) {
        List<Artwork> artworks = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Log.i("UtilsForJson", "artistDisplayName: " + jsonObject.getString("artistDisplayName"));

                Log.i("UtilsForJson", "artistName: " + artistName);
                String artistDisplayName = jsonObject.getString("artistDisplayName").toLowerCase();

                // Filtrar por artista y verificar que tenga imágenes
                if (artistDisplayName.equals(artistName.toLowerCase())){

                    Artwork artwork = new Artwork(
                            jsonObject.getInt("objectID"),
                            jsonObject.getString("primaryImage"),
                            jsonObject.getString("primaryImageSmall"),
                            jsonObject.getString("objectName"),
                            jsonObject.getString("title"),
                            jsonObject.getString("culture"),
                            jsonObject.getString("period"),
                            jsonObject.getString("artistDisplayName"),
                            jsonObject.getString("artistDisplayBio")
                    );
                    artworks.add(artwork);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    public static List<Artwork> convertToArtworks(List<JSONObject> jsonObjects) {
        List<Artwork> artworks = new ArrayList<>();

        try {
            for (JSONObject jsonObject : jsonObjects) {
                Log.i("UtilsForJson", "artistDisplayName: " + jsonObject.optString("artistDisplayName"));

                // Usamos el nombre del artista proporcionado en lugar de lo que esté en el JSON
                String artistDisplayName = "Descubrimientos";

                // Comprobar si las imágenes no están vacías
                String primaryImage = jsonObject.optString("primaryImage");
                String primaryImageSmall = jsonObject.optString("primaryImageSmall");

                // Solo agregar el Artwork si ambas imágenes no están vacías
                if (primaryImage != null && !primaryImage.isEmpty() && primaryImageSmall != null && !primaryImageSmall.isEmpty()) {
                    Artwork artwork = new Artwork(
                            jsonObject.optInt("objectID"),
                            primaryImage,
                            primaryImageSmall,
                            jsonObject.optString("objectName"),
                            jsonObject.optString("title"),
                            jsonObject.optString("culture"),
                            jsonObject.optString("period"),
                            artistDisplayName,  // Siempre usa el nombre de artista proporcionado
                            jsonObject.optString("artistDisplayBio")
                    );
                    artworks.add(artwork);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artworks;
    }

}

