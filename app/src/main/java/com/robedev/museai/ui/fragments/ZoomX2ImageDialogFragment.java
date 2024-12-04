package com.robedev.museai.ui.fragments;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

public class ZoomX2ImageDialogFragment extends DialogFragment {
    private static final String ARG_IMAGE_URL = "image_url";

    private ImageView imageView;
    private Matrix matrix = new Matrix();
    private float scaleFactor = 1f;

    // Valores para desplazamiento
    private float translateX = 0f;
    private float translateY = 0f;

    // Límites
    private static final float MAX_SCALE = 4f;
    private static final float MIN_SCALE = 1f;

    public static ZoomX2ImageDialogFragment newInstance(String imageUrl) {
        ZoomX2ImageDialogFragment fragment = new ZoomX2ImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(getActivity());
        imageView = new ImageView(getActivity());

        // Configurar el ImageView
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(imageView);

        if (getArguments() != null) {
            String imageUrl = getArguments().getString(ARG_IMAGE_URL);
            Glide.with(this).load(imageUrl).into(imageView);
        }

        // Escuchar teclas del mando
        layout.setFocusableInTouchMode(true);
        layout.requestFocus();
        layout.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER: // Botón clic (centro)
                        zoomIn();
                        return true;

                    case KeyEvent.KEYCODE_DPAD_DOWN: // Flecha arriba
                        translateY -= 50; // Ajusta el valor según la velocidad de desplazamiento
                        applyTransformation();
                        return true;

                    case KeyEvent.KEYCODE_DPAD_UP: // Flecha abajo
                        translateY += 50;
                        applyTransformation();
                        return true;

                    case KeyEvent.KEYCODE_DPAD_RIGHT: // Flecha izquierda
                        translateX -= 50;
                        applyTransformation();
                        return true;

                    case KeyEvent.KEYCODE_DPAD_LEFT: // Flecha derecha
                        translateX += 50;
                        applyTransformation();
                        return true;
                }
            }
            return false;
        });

        return layout;
    }

    private void zoomIn() {
        scaleFactor *= 1.1f;
        if (scaleFactor > MAX_SCALE) {
            scaleFactor = MAX_SCALE;
        }
        applyTransformation();
    }

    private void applyTransformation() {
        // Aplicar la matriz de transformación (zoom y desplazamiento)
        matrix.reset();
        matrix.setScale(scaleFactor, scaleFactor);
        matrix.postTranslate(translateX, translateY);
        imageView.setImageMatrix(matrix);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setGravity(Gravity.CENTER);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
    }
}
