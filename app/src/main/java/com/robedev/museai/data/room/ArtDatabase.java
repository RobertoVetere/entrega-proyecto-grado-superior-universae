package com.robedev.museai.data.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.data.model.Collection;
import com.robedev.museai.data.model.ArtworkCollectionCrossRef;

@Database(entities = {Artwork.class, Collection.class, ArtworkCollectionCrossRef.class}, version = 3, exportSchema = false)
public abstract class ArtDatabase extends RoomDatabase {
    private static ArtDatabase instance;

    public abstract ArtworkDao artworkDao();
    public abstract CollectionDao collectionDao();
    public abstract ArtworkCollectionCrossRefDao crossRefDao();

    public static synchronized ArtDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ArtDatabase.class, "met_database")
                    //.fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
