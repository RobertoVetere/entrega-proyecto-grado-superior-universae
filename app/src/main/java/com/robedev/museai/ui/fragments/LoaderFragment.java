package com.robedev.museai.ui.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.robedev.museai.R;

public class LoaderFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView textView;

    public LoaderFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Crear el ProgressBar (Spinner)
        progressBar = new ProgressBar(getContext());
        layout.addView(progressBar);

        // Crear el TextView de mensaje
        textView = new TextView(getContext());
        textView.setVisibility(View.GONE);

        layout.addView(textView);

        return layout;
    }

    public void setLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public void setErrorMessage(String message) {
        progressBar.setVisibility(View.GONE);
        textView.setText(message);
        textView.setVisibility(View.VISIBLE);
    }
}
