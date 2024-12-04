package com.robedev.museai.service;
import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.data.room.ArtworkDao;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ArtworkService {
/*
    private ArtworkDao artworkDao;

    public ArtworkService(ArtworkDao artworkDao) {
        this.artworkDao = artworkDao;
    }

    public void processArtworks(List<Integer> objectIDs) {
        List<Artwork> artworksToSave = new ArrayList<>();

        for (int objectID : objectIDs) {
            JSONObject artworkJson = fetchArtworkDetails(objectID);
            if (shouldSaveArtwork(artworkJson)) {
                Artwork artwork = createArtworkFromJson(artworkJson);
                artworksToSave.add(artwork);
            }
        }

        saveArtworksToDatabase(artworksToSave);
    }

    private JSONObject fetchArtworkDetails(int objectID) {
        // Implementa la lógica para obtener la información del arte desde la API
        // y devolver un objeto JSON con la información.
        // Este es solo un ejemplo, debes reemplazarlo con tu implementación real.
        return new JSONObject();
    }

    private boolean shouldSaveArtwork(JSONObject artworkJson) {
        String primaryImage = artworkJson.optString("primaryImage", "");
        String primaryImageSmall = artworkJson.optString("primaryImageSmall", "");
        String artistDisplayName = artworkJson.optString("artistDisplayName", "");

        return !primaryImage.isEmpty() && !primaryImageSmall.isEmpty() && "Pablo Picasso".equals(artistDisplayName);
    }

    private Artwork createArtworkFromJson(JSONObject artworkJson) {
        int objectID = artworkJson.optInt("objectID", 0);
        String primaryImage = artworkJson.optString("primaryImage", "");
        String primaryImageSmall = artworkJson.optString("primaryImageSmall", "");
        String objectName = artworkJson.optString("objectName", "");
        String title = artworkJson.optString("title", "");
        String culture = artworkJson.optString("culture", "");
        String period = artworkJson.optString("period", "");
        String artistDisplayName = artworkJson.optString("artistDisplayName", "");
        String artistDisplayBio = artworkJson.optString("artistDisplayBio", "");

        return new Artwork(objectID, primaryImage, primaryImageSmall, objectName, title, culture, period, artistDisplayName, artistDisplayBio);
    }

    private void saveArtworksToDatabase(List<Artwork> artworks) {
        artworkDao.insertAll(artworks);
    }

 */
}
