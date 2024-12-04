package com.robedev.museai.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.robedev.museai.enums.CollectionType;

/*
 * Collection class represents a collection entity with title, description, image thumbs, and video URL.
 */
@Entity(tableName = "collections")
public class Collection implements Parcelable {
    static final long serialVersionUID = 727566175075960653L;

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private String bgImageUrl;
    private String cardImageUrl;
    private String videoUrl;
    private String studio;

    private CollectionType collectionType;

    public Collection() {

        this.collectionType = CollectionType.STANDARD;
    }

    protected Collection(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        bgImageUrl = in.readString();
        cardImageUrl = in.readString();
        videoUrl = in.readString();
        studio = in.readString();

        collectionType = (CollectionType) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(bgImageUrl);
        dest.writeString(cardImageUrl);
        dest.writeString(videoUrl);
        dest.writeString(studio);

        dest.writeSerializable(collectionType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel in) {
            return new Collection(in);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        if (collectionType == null) {
            this.collectionType = CollectionType.STANDARD;  // Valor por defecto
        } else {
            this.collectionType = collectionType;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", bgImageUrl='" + bgImageUrl + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", studio='" + studio + '\'' +
                ", collectionType=" + collectionType +  // Agregar el tipo de colección
                '}';
    }
}
