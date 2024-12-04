package com.robedev.museai.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artworks")
public class Artwork implements Parcelable {
    @PrimaryKey
    private final int objectID;
    private String primaryImage;
    private String primaryImageSmall;
    private String objectName;
    private String title;
    private String culture;
    private String period;
    private String artistDisplayName;
    private String artistDisplayBio;
    public Artwork(int objectID, String primaryImage, String primaryImageSmall, String objectName, String title, String culture, String period, String artistDisplayName, String artistDisplayBio) {
        this.objectID = objectID;
        this.primaryImage = primaryImage;
        this.primaryImageSmall = primaryImageSmall;
        this.objectName = objectName;
        this.title = title;
        this.culture = culture;
        this.period = period;
        this.artistDisplayName = artistDisplayName;
        this.artistDisplayBio = artistDisplayBio;
    }

    protected Artwork(Parcel in) {
        objectID = in.readInt();
        primaryImage = in.readString();
        primaryImageSmall = in.readString();
        objectName = in.readString();
        title = in.readString();
        culture = in.readString();
        period = in.readString();
        artistDisplayName = in.readString();
        artistDisplayBio = in.readString();
    }

    public int getObjectID() {
        return objectID;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getPrimaryImageSmall() {
        return primaryImageSmall;
    }

    public void setPrimaryImageSmall(String primaryImageSmall) {
        this.primaryImageSmall = primaryImageSmall;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getArtistDisplayName() {
        return artistDisplayName;
    }

    public void setArtistDisplayName(String artistDisplayName) {
        this.artistDisplayName = artistDisplayName;
    }

    public String getArtistDisplayBio() {
        return artistDisplayBio;
    }

    public void setArtistDisplayBio(String artistDisplayBio) {
        this.artistDisplayBio = artistDisplayBio;
    }


    @Override
    public String toString() {
        return "Artwork{" +
                "objectID=" + objectID +
                ", primaryImage='" + primaryImage + '\'' +
                ", primaryImageSmall='" + primaryImageSmall + '\'' +
                ", objectName='" + objectName + '\'' +
                ", title='" + title + '\'' +
                ", culture='" + culture + '\'' +
                ", period='" + period + '\'' +
                ", artistDisplayName='" + artistDisplayName + '\'' +
                ", artistDisplayBio='" + artistDisplayBio + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(objectID);
        parcel.writeString(primaryImage);
        parcel.writeString(primaryImageSmall);
        parcel.writeString(objectName);
        parcel.writeString(title);
        parcel.writeString(culture);
        parcel.writeString(period);
        parcel.writeString(artistDisplayName);
        parcel.writeString(artistDisplayBio);
    }

    public static final Creator<Artwork> CREATOR = new Creator<Artwork>() {
        @Override
        public Artwork createFromParcel(Parcel in) {
            return new Artwork(in);
        }

        @Override
        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };
}
