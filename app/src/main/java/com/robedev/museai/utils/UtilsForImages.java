package com.robedev.museai.utils;

import android.view.ViewGroup;

import androidx.leanback.widget.ImageCardView;

public class UtilsForImages {

    public static void adjustCardViewDimensions(ImageCardView cardView, int width, int height) {
        // Calcular la proporción de la imagen
        float aspectRatio = (float) width / height;

        // Establecer un ancho y altura máximos para el cardView
        int maxWidth = 1520; // Puedes ajustar este valor según tus necesidades
        int maxHeight = 900; // Puedes ajustar este valor según tus necesidades

        // Calcular la altura según la proporción
        int adjustedHeight = (int) (maxWidth / aspectRatio);

        // Limitar la altura máxima
        if (adjustedHeight > maxHeight) {
            adjustedHeight = maxHeight;
        }

        // Ajustar el tamaño del cardView
        ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
        layoutParams.width = maxWidth;
        layoutParams.height = adjustedHeight;
        cardView.setLayoutParams(layoutParams);
    }
}
