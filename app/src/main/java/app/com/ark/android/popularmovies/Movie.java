package app.com.ark.android.popularmovies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ark on 7/29/2015.
 */
public class Movie implements Parcelable{

    // parcel keys
    private static final String KEY_BACKPATH = "Backdrop_path";
    private static final String KEY_ID = "Id";
    private static final String KEY_LANG = "Lang";
    private static final String KEY_OTITLE = "OTitle";
    private static final String KEY_OVERVIEW = "Overview";
    private static final String KEY_RELEASEDATE = "ReleaseDate";
    private static final String KEY_POSTERPATH = "Poster_path";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_VIDEO = "Video";
    private static final String KEY_VOTEAVG = "VoteAvg";
    private static final String KEY_VOTECNT = "Votecnt";

    private String mBackdrop_path=null;
    private String mId=null;
    private String mLang="Unknown";
    private String mOTitle="Unknown";
    private String mOverview="Not Available";
    private String mReleaseDate="TBD";
    private String mPoster_path=null;
    private String mTitle="Unknown";
    private String mVideo=null;
    private String mVoteAvg="Unknown";
    private String mVotecnt=null;

    public Movie(){};

    /**
     * @param bpath
     * @param id
     * @param lang
     * @param otitle
     * @param overview
     * @param repleasedate
     * @param ppath
     * @param title
     * @param video
     * @param voteavg
     * @param votecnt
     */
    public Movie(String bpath, String id,String lang,String otitle,String overview,String repleasedate,String ppath,String title,String video,String voteavg,String votecnt) {
        this.mBackdrop_path = bpath;
        this.mId = id;
        this.mLang = lang;
        this.mOTitle = otitle;
        this.mOverview = overview;
        this.mReleaseDate = repleasedate;
        this.mPoster_path = ppath;
        this.mTitle = title;
        this.mVideo = video;
        this.mVoteAvg = voteavg;
        this.mVotecnt = votecnt;
    }

    public String getmBackdrop_path() {
        return mBackdrop_path;
    }

    public void setmBackdrop_path(String mBackdrop_path) {
        this.mBackdrop_path = mBackdrop_path;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmLang() {
        return mLang;
    }

    public void setmLang(String mLang) {
        this.mLang = mLang;
    }

    public String getmOTitle() {
        return mOTitle;
    }

    public void setmOTitle(String mOTitle) {
        this.mOTitle = mOTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        if(mReleaseDate!="null"){
            this.mReleaseDate = mReleaseDate;
        }
    }

    public String getmPoster_path() {
        return mPoster_path;
    }

    public void setmPoster_path(String mPoster_path) {
        this.mPoster_path = mPoster_path;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmVideo() {
        return mVideo;
    }

    public void setmVideo(String mVideo) {
        this.mVideo = mVideo;
    }

    public String getmVoteAvg() {
        return mVoteAvg;
    }

    public void setmVoteAvg(String mVoteAvg) {
        this.mVoteAvg = mVoteAvg;
    }

    public String getmVotecnt() {
        return mVotecnt;
    }

    public void setmVotecnt(String mVotecnt) {
        this.mVotecnt = mVotecnt;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        bundle.putString(KEY_BACKPATH,mBackdrop_path);
        bundle.putString(KEY_ID,mId);
        bundle.putString(KEY_LANG,mLang);
        bundle.putString(KEY_OTITLE,mOTitle);
        bundle.putString(KEY_OVERVIEW,mOverview);
        bundle.putString(KEY_RELEASEDATE,mReleaseDate);
        bundle.putString(KEY_POSTERPATH,mPoster_path);
        bundle.putString(KEY_TITLE,mTitle);
        bundle.putString(KEY_VIDEO,mVideo);
        bundle.putString(KEY_VOTEAVG,mVoteAvg);
        bundle.putString(KEY_VOTECNT,mVotecnt);

        dest.writeBundle(bundle);
    }

    /**
     * Creator required for class implementing the parcelable interface.
     */
    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            // read the bundle containing key value pairs from the parcel
            Bundle bundle = source.readBundle();

            // instantiate a person using values from the bundle
            return new Movie(
                    bundle.getString(KEY_BACKPATH),
                    bundle.getString(KEY_ID),
                    bundle.getString(KEY_LANG),
                    bundle.getString(KEY_OTITLE),
                    bundle.getString(KEY_OVERVIEW),
                    bundle.getString(KEY_RELEASEDATE),
                    bundle.getString(KEY_POSTERPATH),
                    bundle.getString(KEY_TITLE),
                    bundle.getString(KEY_VIDEO),
                    bundle.getString(KEY_VOTEAVG),
                    bundle.getString(KEY_VOTECNT));
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

    };

}
