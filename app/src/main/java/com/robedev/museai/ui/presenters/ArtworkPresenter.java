package com.robedev.museai.ui.presenters;

import android.graphics.drawable.Drawable;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.robedev.museai.R;
import com.robedev.museai.data.model.Artwork;

public class ArtworkPresenter extends Presenter {
    private static final String TAG = "ArtworkPresenter";

    private static final int CARD_WIDTH = 413;
    private static final int CARD_HEIGHT = 276;
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
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.selected_background);
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
        cardView.setBackgroundResource(R.drawable.rounded_card_background);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Artwork artwork = (Artwork) item; // Cambia Collection a Artwork
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        Log.d(TAG, "onBindViewHolder");
        if (artwork.getPrimaryImage() != null) {
            cardView.setTitleText(artwork.getTitle());
            cardView.setContentText(artwork.getArtistDisplayName());
            cardView.setContentText(artwork.getArtistDisplayName());
            cardView.setContentText(artwork.getArtistDisplayBio());
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            Glide.with(viewHolder.view.getContext())
                    .load(artwork.getPrimaryImageSmall())
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Usar caché de disco
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}