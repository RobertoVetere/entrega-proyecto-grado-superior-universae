package com.robedev.museai.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "artwork_collection_cross_ref",
        primaryKeys = {"objectID", "collectionID"},
        foreignKeys = {
                @ForeignKey(entity = Artwork.class, parentColumns = "objectID", childColumns = "objectID"),
                @ForeignKey(entity = Collection.class, parentColumns = "id", childColumns = "collectionID")
        },
        indices = {@Index("objectID"), @Index("collectionID")})
public class ArtworkCollectionCrossRef {
    private int objectID;
    private long collectionID;

    // Constructor, getters y setters

    public ArtworkCollectionCrossRef(int objectID, long collectionID) {
        this.objectID = objectID;
        this.collectionID = collectionID;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int objectID) {
        this.objectID = objectID;
    }

    public long getCollectionID() {
        return collectionID;
    }

    public void setCollectionID(long collectionID) {
        this.collectionID = collectionID;
    }
}

