package com.example.android.topmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 06-Mar-18.
 */

public class RvReviewsItem implements Parcelable {

    private String mReviewAuthor;
    private String mReviewContent;

    public RvReviewsItem(String mReviewAuthor, String mReviewContent) {
        this.mReviewAuthor = mReviewAuthor;
        this.mReviewContent = mReviewContent;
    }

    public String getmReviewAuthor() {
        return mReviewAuthor;
    }

    public String getmReviewContent() {
        return mReviewContent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mReviewAuthor);
        dest.writeString(this.mReviewContent);
    }

    protected RvReviewsItem(Parcel in) {
        this.mReviewAuthor = in.readString();
        this.mReviewContent = in.readString();
    }

    public static final Parcelable.Creator<RvReviewsItem> CREATOR = new Parcelable.Creator<RvReviewsItem>() {
        @Override
        public RvReviewsItem createFromParcel(Parcel source) {
            return new RvReviewsItem(source);
        }

        @Override
        public RvReviewsItem[] newArray(int size) {
            return new RvReviewsItem[size];
        }
    };
}
