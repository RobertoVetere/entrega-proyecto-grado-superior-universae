package com.robedev.museai.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.robedev.museai.data.model.ArtworkCollectionCrossRef;

import java.util.List;

@Dao
public interface ArtworkCollectionCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ArtworkCollectionCrossRef crossRef);

    @Query("SELECT * FROM artwork_collection_cross_ref WHERE objectID = :objectID")
    List<ArtworkCollectionCrossRef> getCrossRefsByArtworkId(int objectID);

    @Query("SELECT * FROM artwork_collection_cross_ref WHERE collectionID = :collectionID")
    List<ArtworkCollectionCrossRef> getCrossRefsByCollectionId(long collectionID);
}
