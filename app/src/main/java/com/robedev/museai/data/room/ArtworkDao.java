package com.robedev.museai.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.robedev.museai.data.model.Artwork;

import java.util.List;

@Dao
public interface ArtworkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Artwork artwork);

    @Query("SELECT * FROM artworks")
    List<Artwork> getAllArtworks();

    @Query("SELECT * FROM artworks WHERE objectID = :id")
    Artwork getArtworkById(int id);

    @Query("SELECT * FROM artworks WHERE artistDisplayName LIKE '%' || :artistName || '%'")
    List<Artwork> getArtworksByArtist(String artistName);

    // Lista de obras por período
    @Query("SELECT * FROM artworks WHERE period = :period")
    List<Artwork> getArtworksByPeriod(String period);

    // Lista de obras por cultura
    @Query("SELECT * FROM artworks WHERE culture = :culture")
    List<Artwork> getArtworksByCulture(String culture);

    @Query("DELETE FROM artworks") // Cambia "artwork" al nombre real de tu tabla si es diferente
    void clearAll();

    @Query("SELECT COUNT(objectID) FROM artworks")
    int getArtworkCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Artwork> artworks);

    @Transaction
    @Query("SELECT * FROM artworks WHERE objectID IN (SELECT objectID FROM artwork_collection_cross_ref WHERE collectionID = (SELECT id FROM collections WHERE title = :collectionName))")
    List<Artwork> getArtworksByCollection(String collectionName);


}
