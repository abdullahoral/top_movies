package com.example.android.topmovies;

/**
 * Created by User on 06-Mar-18.
 */

public class RvMainItem {

    private String mImageUrl;
    private String mOriginalTitle;
    private String mOverview;
    private int mVoteAverage;
    private String mReleaseDate;


    public RvMainItem(String mImageUrl, String mOriginalTitle, String mOverview, int mVoteAverage, String mReleaseDate) {
        this.mImageUrl = mImageUrl;
        this.mOriginalTitle = mOriginalTitle;
        this.mOverview = mOverview;
        this.mVoteAverage = mVoteAverage;
        this.mReleaseDate = mReleaseDate;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public int getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(int mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
