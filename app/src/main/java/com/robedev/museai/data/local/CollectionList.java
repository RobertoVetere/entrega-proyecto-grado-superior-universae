package com.robedev.museai.data.local;

import android.content.Context;

import com.robedev.museai.R;
import com.robedev.museai.data.model.Collection;
import com.robedev.museai.enums.CollectionType;

import java.util.ArrayList;
import java.util.List;

public final class CollectionList {

    public static final String PIECE_CATEGORY[] = {
            "Impresionismo",
            "Postimpresionismo",
            "Arte del Renacimiento",
            //"Arte Americano",
            //"Arte Asiático",
            //"Arte Africano",
            //"Arte del Medio Oriente",
            //"Arte Contemporáneo",
            //"Fotografía",
            //"Arte Decorativo",
            "Arte al azar",
            //"Tu Galería",
    };

    private static List<Collection> list;
    private static long count = 0;



    public static List<Collection> getMuseumCollections(Context context, String museum) {
        switch (museum) {
            case "Impresionismo":
                return setupImpressionism(context);
            case "Postimpresionismo":
                return setupPostImpressionism(context);
            case "Arte del Renacimiento":
                return setupRenaissance(context);
            case "Arte Americano":
                return setupPostImpressionism(context);
            case "Arte Asiático":
                return setupPostImpressionism(context);
            case "Arte Africano":
                return setupPostImpressionism(context);
            case "Arte del Medio Oriente":
                return setupPostImpressionism(context);
            case "Arte Contemporáneo":
                return setupPostImpressionism(context);
            case "Fotografía":
                return setupPostImpressionism(context);
            case "Arte Decorativo":
                return setupPostImpressionism(context);
            case "Tu Galería":
                return myGallery(context);
            case "Arte al azar":
                return arteAlAzar(context);
            default:
                return new ArrayList<>(); // Retorna una lista vacía si no se encuentra el museo.
        }
    }

    private static List<Collection> arteAlAzar(Context context) {
        List<Collection> list = new ArrayList<>();
        Collection collection = buildCollectionInfo(
                "Descubre nuevas obras cada día",
                context.getString(R.string.description_colection),
                "Arte al Azar",
                "url_video_1",
                "https://images.metmuseum.org/CRDImages/ep/web-large/DP259921.jpg",
                "https://images.metmuseum.org/CRDImages/ep/web-large/DP259921.jpg"
        );
        // Asignar tipo de colección RANDOMART
        collection.setCollectionType(CollectionType.RANDOMART);
        list.add(collection);
        return list;
    }

    private static List<Collection> myGallery(Context context) {
        List<Collection> list = new ArrayList<>();
        Collection collection = buildCollectionInfo(
                "Mi primera colección",
                context.getString(R.string.description_colection),
                "Tu Galería",
                "url_video_1",
                "https://images.metmuseum.org/CRDImages/rl/web-large/DP809590.jpg",
                "https://images.metmuseum.org/CRDImages/rl/web-large/DP809590.jpg"
        );
        // Asignar tipo de colección MYGALLERY
        collection.setCollectionType(CollectionType.MYGALLERY);
        list.add(collection);
        return list;
    }

    private static List<Collection> setupImpressionism(Context context) {
        List<Collection> list = new ArrayList<>();
        list.add(buildCollectionInfo("Auguste Renoir", context.getString(R.string.description_renoir), "Impressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DP-23203-001.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DT1878.jpg"));
        list.add(buildCollectionInfo("Edouard Manet", context.getString(R.string.description_manet), "Impressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DP-25466-001.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DP-25459-001.jpg"));
        list.add(buildCollectionInfo("Rembrandt (Rembrandt van Rijn)", context.getString(R.string.description_rembrandt), "Impressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DP145921.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DP146482.jpg"));
        //list.add(buildCollectionInfo("Arte al Azar", context.getString(R.string.arte_al_azar), "Impressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DP145921.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DP146482.jpg"));
        return list;
    }

    private static List<Collection> setupPostImpressionism(Context context) {
        List<Collection> list = new ArrayList<>();
        list.add(buildCollectionInfo("Vincent van Gogh", context.getString(R.string.description_van_gogh), "Postimpressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DP130999.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DT1567.jpg"));
        list.add(buildCollectionInfo("Paul Cézanne", context.getString(R.string.description_paul_cezanne), "Postimpressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DT1944.jpg", "https://images.metmuseum.org/CRDImages/dp/original/DT4962.jpg"));
        list.add(buildCollectionInfo("Paul Gauguin", context.getString(R.string.description_paul_gauguin), "Postimpressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DT1342.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DT1952.jpg"));
        list.add(buildCollectionInfo("Georges Seurat", context.getString(R.string.description_georges_seurat), "Postimpressionism", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DP259921.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DP375450_cropped.jpg"));

        return list;
    }

    private static List<Collection> setupRenaissance(Context context) {
        List<Collection> list = new ArrayList<>();
        list.add(buildCollectionInfo("Leonardo Da Vinci", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/dp/web-large/DP832657.jpg", "https://images.metmuseum.org/CRDImages/dp/original/DP102382.jpg"));
        list.add(buildCollectionInfo("Botticelli (Alessandro di Mariano Filipepi)", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/rl/web-large/DT2634.jpg", "https://images.metmuseum.org/CRDImages/rl/original/DT2634.jpg"));
        list.add(buildCollectionInfo("Michelangelo Buonarroti", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/dp/web-large/DP826907.jpg", "https://images.metmuseum.org/CRDImages/es/original/DP-936-001.jpg"));
        list.add(buildCollectionInfo("Albrecht Dürer", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/rl/web-large/DP809590.jpg", "https://images.metmuseum.org/CRDImages/rl/original/DP809590.jpg"));
        //list.add(buildCollectionInfo("Donatello", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/es/web-large/DP242101.jpg", "https://images.metmuseum.org/CRDImages/es/original/DP-1368-001.jpg"));
        list.add(buildCollectionInfo("Caravaggio (Michelangelo Merisi)", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/original/DP-12413-001.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DP-687-001.jpg"));
        //list.add(buildCollectionInfo("Raphael", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/dp/web-large/DP862672.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DP136373.jpg"));
        list.add(buildCollectionInfo("Titian (Tiziano Vecellio)", context.getString(R.string.description_georges_seurat), "Renaissance", "url_video_1", "https://images.metmuseum.org/CRDImages/ep/web-large/DP-25977-001.jpg", "https://images.metmuseum.org/CRDImages/ep/original/DP-17398-001.jpg"));

        return list;
    }


    private static Collection buildCollectionInfo(String title, String description, String studio, String videoUrl, String cardImageUrl, String backgroundImageUrl) {
        Collection collection = new Collection();
        collection.setId(count++);
        collection.setTitle(title);
        collection.setDescription(description);
        collection.setStudio(studio);
        collection.setCardImageUrl(cardImageUrl);
        collection.setBgImageUrl(backgroundImageUrl);
        collection.setVideoUrl(videoUrl);
        return collection;
    }
}
