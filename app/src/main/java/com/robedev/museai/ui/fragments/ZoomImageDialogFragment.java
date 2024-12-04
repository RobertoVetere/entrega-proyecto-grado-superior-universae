package com.robedev.museai.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

public class ZoomImageDialogFragment extends DialogFragment {
    private static final String ARG_IMAGE_URL = "image_url";

    public static ZoomImageDialogFragment newInstance(String imageUrl) {
        ZoomImageDialogFragment fragment = new ZoomImageDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(getActivity());
        ImageView imageView = new ImageView(getActivity());

        // Configurar el ImageView
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        layout.addView(imageView);

        if (getArguments() != null) {
            String imageUrl = getArguments().getString(ARG_IMAGE_URL);
            Glide.with(this).load(imageUrl).into(imageView);
        }

        // Detectar clic en la imagen para cambiar a la segunda vista
        imageView.setOnClickListener(v -> {
            // Crear una nueva instancia de ZoomX2ImageDialogFragment
            ZoomX2ImageDialogFragment zoomX2Fragment = ZoomX2ImageDialogFragment.newInstance(getArguments().getString(ARG_IMAGE_URL));
            // Mostrar el fragmento de zoom avanzado
            zoomX2Fragment.show(getChildFragmentManager(), "zoomX2Fragment");
        });

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            // Configura el diálogo para que ocupe toda la pantalla
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getDialog().getWindow().setGravity(Gravity.CENTER);

            // Quita el fondo del diálogo
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        }
    }
}
