package com.robedev.museai.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.robedev.museai.ui.fragments.PlaybackVideoFragment;

/**
 * Loads {@link PlaybackVideoFragment}.
 */
public class PlaybackActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new PlaybackVideoFragment())
                    .commit();
        }
    }
}