package com.robedev.museai.ui.presenters;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.robedev.museai.data.model.Collection;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Collection collection = (Collection) item;

        if (collection != null) {
            viewHolder.getTitle().setText(collection.getTitle());
            viewHolder.getSubtitle().setText(collection.getStudio());
            viewHolder.getBody().setText(collection.getDescription());
        }
    }
}