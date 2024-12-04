package com.robedev.museai.ui.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.data.model.Collection;
import com.robedev.museai.enums.CollectionType;
import com.robedev.museai.service.RandomArtService;
import com.robedev.museai.ui.activity.CollectionDetActivity;
import com.robedev.museai.ui.presenters.ArtworkPresenter;
import com.robedev.museai.ui.presenters.DetailsDescriptionPresenter;
import com.robedev.museai.ui.activity.MainActivity;
import com.robedev.museai.ui.activity.DetailsActivity;
import com.robedev.museai.R;
import com.robedev.museai.utils.Utils;
import com.robedev.museai.utils.UtilsForJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CollectionLobbyFragment extends DetailsSupportFragment {
    private static final String TAG = "CollectionLobbyFragment";
    private Collection mSelectedCollection;
    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;
    private DetailsSupportFragmentBackgroundController mDetailsBackground;
    private List<Artwork> artworks;
    private CollectionType collectionType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);
        mSelectedCollection = getActivity().getIntent().getParcelableExtra(DetailsActivity.COLLECTION);

        // Obtener el tipo de colección
        collectionType = (CollectionType) getActivity().getIntent().getSerializableExtra("COLLECTION_TYPE");

        if (mSelectedCollection != null) {
            mPresenterSelector = new ClassPresenterSelector();
            mAdapter = new ArrayObjectAdapter(mPresenterSelector);
            setupUIElements();
            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();
            setAdapter(mAdapter);
            initializeBackground(mSelectedCollection);
            setOnItemViewClickedListener(new ItemViewClickedListener());


            //handleCollectionType(collectionType); // Llamamos a un método que maneja el comportamiento según el tipo

            loadArtworks();
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void setupUIElements() {
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_app_logo));
        setTitle(mSelectedCollection != null ? mSelectedCollection.getTitle() : getString(R.string.browse_title));
    }

    private void initializeBackground(Collection data) {
        mDetailsBackground.enableParallax();
        Glide.with(getActivity())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(data.getBgImageUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        // Asegúrate de actualizar la UI en el hilo principal
                        getActivity().runOnUiThread(() -> {
                            mDetailsBackground.setCoverBitmap(bitmap);
                            mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                        });
                    }
                });
    }

    private void setupDetailsOverviewRow() {
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedCollection);
        row.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_background));

        Glide.with(getActivity())
                .load(mSelectedCollection.getCardImageUrl())
                .centerCrop()
                .error(R.drawable.default_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                        row.setImageDrawable(drawable);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();
        actionAdapter.add(new Action(1, getResources().getString(R.string.watch_trailer_1), getResources().getString(R.string.watch_trailer_2)));
        row.setActionsAdapter(actionAdapter);
        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter() {
        FullWidthDetailsOverviewRowPresenter detailsPresenter = new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selected_background));

        FullWidthDetailsOverviewSharedElementHelper sharedElementHelper = new FullWidthDetailsOverviewSharedElementHelper();
        sharedElementHelper.setSharedElementEnterTransition(getActivity(), DetailsActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == 1) {
                    Intent intent = new Intent(getActivity(), CollectionDetActivity.class);
                    intent.putExtra(DetailsActivity.COLLECTION, mSelectedCollection);
                    intent.putParcelableArrayListExtra(DetailsActivity.ARTWORKS, new ArrayList<>(artworks));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private void loadArtworks() {
        if (collectionType == CollectionType.STANDARD) {
            String artistName = mSelectedCollection.getTitle();
            String formattedArtistName = Utils.formatArtistNameForFile(artistName);

            String jsonFileName = formattedArtistName + ".json";
            Log.d(TAG, "jsonFileName: " + jsonFileName);

            new Thread(() -> {
                // Leer el archivo JSON desde los assets para el caso STANDARD
                String jsonResult = UtilsForJson.loadJSONFromAsset(getContext(), "collections/" + jsonFileName);

                // Filtrar y parsear las obras de arte del JSON
                artworks = UtilsForJson.getArtworksFromJson(jsonResult, artistName);

                // Asegúrate de actualizar la UI en el hilo principal
                getActivity().runOnUiThread(() -> {
                    if (artworks != null && !artworks.isEmpty()) {
                        setupRelatedArtworkListRow(artworks);
                    } else {
                        Toast.makeText(getActivity(), "No se pudieron cargar las obras de arte", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();

        } else if (collectionType == CollectionType.RANDOMART) {
            new Thread(() -> {
                try {
                    // Crear una instancia del servicio RandomArtService
                    RandomArtService randomArtService = new RandomArtService();

                    // Obtener las artworks aleatorias del servicio
                    artworks = randomArtService.obtenerArtworksPorIdsAleatorios();

                    // Actualizar la UI en el hilo principal
                    getActivity().runOnUiThread(() -> {
                        if (artworks != null && !artworks.isEmpty()) {
                            setupRelatedArtworkListRow(artworks);
                        } else {
                            Toast.makeText(getActivity(), "No se pudieron cargar las obras de arte aleatorias", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (InterruptedException | IOException e) {
                    // Manejo de error si el servicio no puede obtener las obras de arte
                    e.printStackTrace();
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Error al cargar las obras de arte aleatorias", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        }
    }

    private void setupRelatedArtworkListRow(List<Artwork> artworks) {
        getActivity().runOnUiThread(() -> {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new ArtworkPresenter());

            // Crea un ExecutorService con un número de hilos que puedas ajustar según tu necesidad
            ExecutorService executor = Executors.newFixedThreadPool(8); // Puedes ajustar el número de hilos

            for (Artwork artwork : artworks) {
                // Enviar la tarea de precargar cada imagen al ExecutorService
                executor.submit(() -> {
                    try {
                        // Precargar la imagen en un hilo diferente
                        Glide.with(getActivity())
                                .load(artwork.getPrimaryImageSmall())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload(); // Pre-cargar la imagen
                    } catch (Exception e) {
                        e.printStackTrace(); // Manejo de errores si la carga falla
                    }
                });

                // Añadir la obra de arte al adaptador
                listRowAdapter.add(artwork);
            }

            // Cerrar el ExecutorService después de que todas las tareas se hayan enviado
            executor.shutdown();

            // Añadir la lista de obras relacionadas al adaptador
            HeaderItem header = new HeaderItem(1, getString(R.string.related_movies));
            mAdapter.add(new ListRow(header, listRowAdapter));
            mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        // No limpiar la caché de Glide al salir, ya que puede ser costoso
        // Glide.get(getActivity()).clearMemory();
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Collection) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(getResources().getString(R.string.collection), mSelectedCollection);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ((ImageCardView) itemViewHolder.view).getMainImageView(), DetailsActivity.SHARED_ELEMENT_NAME).toBundle();
                getActivity().startActivity(intent, bundle);
            }
        }
    }
}
