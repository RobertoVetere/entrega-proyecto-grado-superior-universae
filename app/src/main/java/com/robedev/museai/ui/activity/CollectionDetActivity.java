package com.robedev.museai.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.data.model.Collection;
import com.robedev.museai.ui.fragments.CollectionDetFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Carga el {@link CollectionDetFragment}.
 */
public class CollectionDetActivity extends FragmentActivity {

    private List<Artwork> artworks;
    private Collection selectedCollection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Hola, CollectionDetActivity ha sido creada", Toast.LENGTH_SHORT).show();

        // Recuperar la lista de artworks del Intent
        artworks = getIntent().getParcelableArrayListExtra(DetailsActivity.ARTWORKS);
        selectedCollection = getIntent().getParcelableExtra(DetailsActivity.COLLECTION);
        if (artworks != null) {
            Toast.makeText(this, "Artworks recuperados: " + artworks.size(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontraron artworks", Toast.LENGTH_SHORT).show();
            artworks = new ArrayList<>(); // Aseguramos que la lista no sea nula
        }

        if (savedInstanceState == null) {
            // Crear un nuevo fragmento y pasarle los artworks
            CollectionDetFragment collectionDetFragment = new CollectionDetFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(DetailsActivity.ARTWORKS, new ArrayList<>(artworks));
            collectionDetFragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, collectionDetFragment)
                    .commit();
        }
    }
}
