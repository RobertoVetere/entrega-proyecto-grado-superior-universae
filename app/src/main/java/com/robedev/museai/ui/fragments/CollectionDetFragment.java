package com.robedev.museai.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.robedev.museai.R;
import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.data.model.Collection;
import com.robedev.museai.data.model.ChatButtonItem; // Asegúrate de importar esto
import com.robedev.museai.ui.activity.DetailsActivity;
import com.robedev.museai.ui.presenters.ArtworkFullPresenter;
import com.robedev.museai.ui.presenters.ChatButtonPresenter; // Importa el nuevo presenter
import com.robedev.museai.utils.CacheUtils;
import com.robedev.museai.utils.Utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CollectionDetFragment extends BrowseSupportFragment {

    private List<Artwork> artworks;
    private Collection selectedCollection;

    private int currentArtworkIndex = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUIElements();

        // Recuperar la lista de artworks desde los argumentos
        if (getArguments() != null) {
            artworks = (List<Artwork>) getArguments().getSerializable(DetailsActivity.ARTWORKS);
            selectedCollection = getArguments().getParcelable(DetailsActivity.COLLECTION);
        }

        // Llama a setupAdapter solo si artworks no es null
        if (artworks != null) {
            setupAdapter();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Limpiar la caché de Glide cuando el fragmento se destruya
        Glide.get(getActivity()).clearMemory();
    }

    private void setupUIElements() {
        // Establecer el logo (badge) en la barra de encabezado
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_app_logo)); // Reemplaza con tu recurso SVG
    }

    private void setupAdapter() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new ArtworkFullPresenter());

        // Crear un ExecutorService para manejar múltiples hilos
        ExecutorService executor = Executors.newFixedThreadPool(8);  // Ajusta el número de hilos según sea necesario

        // Verifica si artworks no es null y no está vacío
        if (artworks != null && !artworks.isEmpty()) {
            for (Artwork artwork : artworks) {
                // Enviar cada tarea de carga de imagen a los hilos del ExecutorService
                executor.submit(() -> {
                    try {
                        Glide.with(getActivity())
                                .load(artwork.getPrimaryImageSmall())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload(); // Precargar la imagen de la obra de arte en segundo plano
                    } catch (Exception e) {
                        e.printStackTrace(); // Manejo de errores
                    }
                });

                // Añadir la obra de arte al adaptador de forma normal
                listRowAdapter.add(artwork);
            }
        } else {
            // Caso cuando artworks es null o vacío
            HeaderItem header = new HeaderItem(0, "No se encontraron artworks");
            rowsAdapter.add(new ListRow(header, new ArrayObjectAdapter())); // Añadir fila vacía
        }

        // Cerrar el ExecutorService después de enviar las tareas
        executor.shutdown();

        String collectionTitle = (artworks != null) ? Utils.formatArtistName(artworks.get(0).getArtistDisplayName()) : "Colección Sin Título";

        // Crea una fila de la lista con el título de la colección
        HeaderItem header = new HeaderItem(0, collectionTitle);
        rowsAdapter.add(new ListRow(header, listRowAdapter));

        // Agregar el botón de chat
        addChatButton(rowsAdapter);

        // Establecer el adaptador en el fragmento
        setAdapter(rowsAdapter);
    }

    private void addChatButton(ArrayObjectAdapter rowsAdapter) {
        // Crear un nuevo elemento de acción para el botón
        HeaderItem buttonHeader = new HeaderItem(1, "");

        ChatButtonClickListener listener = new ChatButtonClickListener();
        ArrayObjectAdapter buttonAdapter = new ArrayObjectAdapter(new ChatButtonPresenter(listener));
        buttonAdapter.add(new ChatButtonItem("¡Abrir Chat!")); // Usar el nuevo modelo

        // Agregar la fila de acciones al adaptador
        rowsAdapter.add(new ListRow(buttonHeader, buttonAdapter));
    }

    private class ChatButtonClickListener implements ChatButtonPresenter.OnButtonClickListener {
        @Override
        public void onButtonClick() {
            // Mostrar el fragmento de chat y pasar la lista de artworks y el índice actual
            ChatFragment chatFragment = ChatFragment.newInstance(artworks, currentArtworkIndex);
            chatFragment.show(getActivity().getSupportFragmentManager(), "ChatFragment");
        }
    }

    public void updateCurrentArtworkIndex(int index) {
        currentArtworkIndex = index;
    }


}
