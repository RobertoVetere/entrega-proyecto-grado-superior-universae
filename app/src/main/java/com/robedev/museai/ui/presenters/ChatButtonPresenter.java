package com.robedev.museai.ui.presenters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.leanback.widget.Presenter;

public class ChatButtonPresenter extends Presenter {

    private final OnButtonClickListener listener;

    public ChatButtonPresenter(OnButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Button button = new Button(parent.getContext());
        button.setText("¡Abrir Chat!");
        button.setOnClickListener(v -> {
            if (listener != null) {
                listener.onButtonClick(); // Llama al método del listener
            }
        });
        return new ViewHolder(button);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Object item) {
        // No es necesario vincular datos adicionales aquí
    }

    @Override
    public void onUnbindViewHolder(ViewHolder holder) {
        // Limpiar si es necesario
    }

    // Interfaz para el listener del botón
    public interface OnButtonClickListener {
        void onButtonClick();
    }
}
