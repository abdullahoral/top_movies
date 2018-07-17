package com.example.android.topmovies;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 06-Mar-18.
 */

@Entity(tableName = "rvMainItems")
public class RvMainItem implements Parcelable {

    private String imageUrl;
    private String originalTitle;
    private String overview;
    private int voteAverage;
    private String releaseDate;
    @PrimaryKey
    private int id;

    public RvMainItem(String imageUrl, String originalTitle, String overview, int voteAverage, String releaseDate, int id) {
        this.imageUrl = imageUrl;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public int getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeInt(this.voteAverage);
        dest.writeString(this.releaseDate);
        dest.writeInt(this.id);
    }

    protected RvMainItem(Parcel in) {
        this.imageUrl = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readInt();
        this.releaseDate = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<RvMainItem> CREATOR = new Creator<RvMainItem>() {
        @Override
        public RvMainItem createFromParcel(Parcel source) {
            return new RvMainItem(source);
        }

        @Override
        public RvMainItem[] newArray(int size) {
            return new RvMainItem[size];
        }
    };
}
