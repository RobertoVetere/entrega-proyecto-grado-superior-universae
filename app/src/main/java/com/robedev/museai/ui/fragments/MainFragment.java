package com.robedev.museai.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.robedev.museai.data.model.Collection;
import com.robedev.museai.enums.CollectionType;
import com.robedev.museai.ui.presenters.CardPresenter;
import com.robedev.museai.data.local.CollectionList;
import com.robedev.museai.R;
import com.robedev.museai.ui.activity.BrowseErrorActivity;
import com.robedev.museai.ui.activity.DetailsActivity;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainFragment extends BrowseSupportFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 3;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler(Looper.myLooper());
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();
    }

    private static final String[] IMAGE_URLS = {"https://images.metmuseum.org/CRDImages/ep/web-large/DP-23203-001.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DT1878.jpg",
            "https://images.metmuseum.org/CRDImages/ep/web-large/DP-25466-001.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DP-25459-001.jpg",
            "https://images.metmuseum.org/CRDImages/ep/web-large/DP145921.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DP146482.jpg",
            "https://images.metmuseum.org/CRDImages/ep/web-large/DP130999.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DT1567.jpg",
            "https://images.metmuseum.org/CRDImages/ep/web-large/DT1944.jpg",
            "https://images.metmuseum.org/CRDImages/dp/original/DT4962.jpg",
            "https://images.metmuseum.org/CRDImages/ep/web-large/DT1342.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DT1952.jpg",
            "https://images.metmuseum.org/CRDImages/ep/web-large/DP259921.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DP375450_cropped.jpg",
            "https://images.metmuseum.org/CRDImages/dp/web-large/DP832657.jpg",
            "https://images.metmuseum.org/CRDImages/dp/original/DP102382.jpg",
            "https://images.metmuseum.org/CRDImages/rl/web-large/DT2634.jpg",
            "https://images.metmuseum.org/CRDImages/rl/original/DT2634.jpg",
            "https://images.metmuseum.org/CRDImages/dp/web-large/DP826907.jpg",
            "https://images.metmuseum.org/CRDImages/es/original/DP-936-001.jpg",
            "https://images.metmuseum.org/CRDImages/rl/web-large/DP809590.jpg",
            "https://images.metmuseum.org/CRDImages/rl/original/DP809590.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DP-12413-001.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DP-687-001.jpg",
            "https://images.metmuseum.org/CRDImages/ep/web-large/DP-25977-001.jpg",
            "https://images.metmuseum.org/CRDImages/ep/original/DP-17398-001.jpg"};
    private void preloadImages() {
        new Thread(() -> {
            Log.d(TAG, "Iniciando precarga de imágenes...");
            for (String url : IMAGE_URLS) {
                try {
                    Glide.with(requireActivity())
                            .downloadOnly()
                            .load(url)
                            .submit()
                            .get();
                    Log.d(TAG, "Imagen precargada: " + url);
                } catch (InterruptedException | ExecutionException e) {
                    Log.e(TAG, "Error al precargar imagen: " + url, e);
                }
            }
            Log.d(TAG, "Precarga de imágenes completada.");
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundTimer != null) {
            mBackgroundTimer.cancel();
        }
        Log.d(TAG, "Liberando recursos de Glide...");
        Glide.get(requireActivity()).clearMemory();
    }

    private void loadRows() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        // Cargar las colecciones para cada museo
        for (String museum : CollectionList.PIECE_CATEGORY) {
            List<Collection> collections = CollectionList.getMuseumCollections(requireActivity(), museum);
            if (collections != null && !collections.isEmpty()) {
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (Collection collection : collections) {
                    listRowAdapter.add(collection);
                }
                HeaderItem header = new HeaderItem(rowsAdapter.size(), museum);
                rowsAdapter.add(new ListRow(header, listRowAdapter));
            }
        }

        // Agregar un header para las preferencias
        HeaderItem gridHeader = new HeaderItem(rowsAdapter.size(), "PREFERENCIAS");
        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add(getResources().getString(R.string.grid_view));
        gridRowAdapter.add(getString(R.string.error_fragment));
        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // Establecer el logo (badge) en la barra de encabezado
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_app_logo)); // Reemplaza con tu recurso SVG

        // Establecer el título
        setTitle(getString(R.string.browse_title));

        // Configuración adicional
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // Establecer el color de fondo de los headers
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
        // Establecer el color del icono de búsqueda
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(view -> Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                .show());

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void updateBackground(String uri) {
        if (uri == null || uri.isEmpty()) {
            mBackgroundManager.setDrawable(mDefaultBackground);
            return;
        }
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(requireActivity())
                .load(uri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                        Log.d(TAG, "Fondo actualizado con éxito.");
                        mBackgroundManager.setDrawable(drawable);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        Log.e(TAG, "Error al cargar el fondo.");
                        mBackgroundManager.setDrawable(mDefaultBackground);
                    }
                });
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Collection) {
                Collection collection = (Collection) item;
                Log.d(TAG, "Item: " + item.toString());

                // Aquí agregamos el tipo de colección al Intent
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.COLLECTION, collection);

                // Suponiendo que Collection tiene un método getType() que devuelve un valor de tipo CollectionType
                CollectionType collectionType = collection.getCollectionType();  // Si tienes este método
                intent.putExtra("COLLECTION_TYPE", collectionType);

                // Realizar la transición de la animación
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                ((ImageCardView) itemViewHolder.view).getMainImageView(),
                                DetailsActivity.SHARED_ELEMENT_NAME)
                        .toBundle();
                getActivity().startActivity(intent, bundle);
            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof Collection) {
                mBackgroundUri = ((Collection) item).getBgImageUrl();
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }
}
