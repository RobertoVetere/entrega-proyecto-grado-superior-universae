package com.robedev.museai.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.robedev.museai.data.model.Collection;

@Dao
public interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Collection collection);

    @Query("SELECT * FROM collections WHERE id = :collectionID")
    Collection getCollectionById(long collectionID);

    @Query("SELECT * FROM collections WHERE title = :title")
    Collection getCollectionByTitle(String title);

    @Query("SELECT * FROM collections WHERE title = :title")
    Collection getCollectionByName(String title);
}
