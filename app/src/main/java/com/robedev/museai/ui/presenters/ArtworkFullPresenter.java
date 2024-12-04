package com.robedev.museai.ui.presenters;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity; // Asegúrate de importar FragmentActivity
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.robedev.museai.R;
import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.ui.fragments.ZoomImageDialogFragment; // Asegúrate de importar tu nuevo fragmento

public class ArtworkFullPresenter extends Presenter {
    private static final String TAG = "ArtworkFullPresenter";
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        view.setBackgroundColor(color);
        view.setInfoAreaBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");
        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.default_background);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.default_background);
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        //cardView.setBackgroundResource(R.drawable.rounded_card_background);

        // Establecer márgenes para separar las tarjetas
        int margin = 400; // Ajusta este valor según sea necesario
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(
                WRAP_CONTENT, WRAP_CONTENT);
        marginLayoutParams.setMargins(margin, -180, margin, 0); // Izquierda, Arriba, Derecha, Abajo
        cardView.setLayoutParams(marginLayoutParams);

        // Ocultar el área de contenido (donde aparece el texto)
        cardView.setContentText(null); // No mostrar el nombre del artista
        cardView.setTitleText(null); // No mostrar el título en el área de tarjeta

        // Añadir OnClickListener para el zoom de la imagen
        cardView.setOnClickListener(v -> {
            Context context = v.getContext();
            if (context instanceof FragmentActivity) {
                Artwork artwork = (Artwork) cardView.getTag(); // Obtener el artwork desde el tag
                if (artwork != null) {
                    String imageUrl = artwork.getPrimaryImage(); // Obtener la URL de la imagen
                    ZoomImageDialogFragment dialogFragment = ZoomImageDialogFragment.newInstance(imageUrl);
                    dialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "ZoomImageDialog");
                }
            }
        });

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Artwork artwork = (Artwork) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        cardView.setTag(artwork); // Guardar el artwork en el tag para obtenerlo en el OnClickListener

        Log.d(TAG, "onBindViewHolder");
        if (artwork.getPrimaryImage() != null) {
            Glide.with(viewHolder.view.getContext())
                    .load(artwork.getPrimaryImage()) // Cargar la imagen grande
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Usar caché de disco
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) // Forzar a obtener las dimensiones originales
                    .error(R.drawable.default_background) // Manejar errores
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            // Obtener las dimensiones de la pantalla
                            WindowManager wm = (WindowManager) viewHolder.view.getContext().getSystemService(Context.WINDOW_SERVICE);
                            DisplayMetrics metrics = new DisplayMetrics();
                            wm.getDefaultDisplay().getMetrics(metrics);

                            int screenHeight = metrics.heightPixels; // Obtener la altura de la pantalla

                            // Escalar la imagen para que use el alto de la pantalla
                            Drawable scaledDrawable = scaleDrawable(resource, screenHeight);

                            // Establecer el título de la obra encima de la imagen
                            cardView.setTitleText(artwork.getTitle());

                            // Cargar la imagen escalada en el ImageCardView
                            cardView.setMainImageDimensions(scaledDrawable.getIntrinsicWidth(), scaledDrawable.getIntrinsicHeight());
                            cardView.setMainImage(scaledDrawable);
                        }
                    });
        } else {
            cardView.setMainImage(mDefaultCardImage);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }

    private Drawable scaleDrawable(Drawable drawable, int maxHeight) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calcular la relación de aspecto
        float aspectRatio = (float) width / height;

        // Ajustar dimensiones usando el alto de la pantalla
        height = maxHeight; // Usar todo el alto de la pantalla
        width = (int) (maxHeight * aspectRatio); // Calcular el ancho manteniendo la proporción

        // Escalar el bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return new BitmapDrawable(scaledBitmap);
    }
}
