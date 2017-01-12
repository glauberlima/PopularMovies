package com.glauber.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by glauberl on 1/10/2017.
 */

public class MovieParcelable implements Parcelable {

    public static final String NAME = MovieParcelable.class.getSimpleName();
    public static final Creator<MovieParcelable> CREATOR = new Creator<MovieParcelable>() {
        @Override
        public MovieParcelable createFromParcel(Parcel in) {
            return new MovieParcelable(in);
        }

        @Override
        public MovieParcelable[] newArray(int size) {
            return new MovieParcelable[size];
        }
    };
    private String title;
    private String posterUrl;
    private int releaseYear;
    private String synopsis;
    private double userRating;

    public MovieParcelable() {

    }

    protected MovieParcelable(Parcel in) {
        title = in.readString();
        posterUrl = in.readString();
        releaseYear = in.readInt();
        synopsis = in.readString();
        userRating = in.readDouble();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(posterUrl);
        parcel.writeInt(releaseYear);
        parcel.writeString(synopsis);
        parcel.writeDouble(userRating);
    }
}