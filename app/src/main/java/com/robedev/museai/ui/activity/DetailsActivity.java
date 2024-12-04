package com.robedev.museai.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.robedev.museai.R;
import com.robedev.museai.ui.fragments.CollectionLobbyFragment;

/*
 * Details activity class that loads LeanbackDetailsFragment class
 */
public class DetailsActivity extends FragmentActivity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String COLLECTION = "Collection";

    public static final String ARTWORKS = "artworks";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, new CollectionLobbyFragment())
                    .commitNow();
        }
    }

}