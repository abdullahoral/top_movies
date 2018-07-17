package com.example.android.topmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 06-Mar-18.
 */

public class RvVideosItem implements Parcelable {

    private String mVideoKey;
    private String mVideoName;

    public RvVideosItem(String mVideoKey, String mVideoName) {
        this.mVideoKey = mVideoKey;
        this.mVideoName = mVideoName;
    }

    public String getmVideoKey() {
        return mVideoKey;
    }

    public String getmVideoName() {
        return mVideoName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mVideoKey);
        dest.writeString(this.mVideoName);
    }

    protected RvVideosItem(Parcel in) {
        this.mVideoKey = in.readString();
        this.mVideoName = in.readString();
    }

    public static final Parcelable.Creator<RvVideosItem> CREATOR = new Parcelable.Creator<RvVideosItem>() {
        @Override
        public RvVideosItem createFromParcel(Parcel source) {
            return new RvVideosItem(source);
        }

        @Override
        public RvVideosItem[] newArray(int size) {
            return new RvVideosItem[size];
        }
    };
}
