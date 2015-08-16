package app.com.ark.android.popularmovies;

import app.com.ark.android.popularmovies.data.MovieContract;

/**
 * Created by ark on 8/12/2015.
 */
public class Constant {
    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_BACKPATH,
            MovieContract.MovieEntry.COLUMN_OTITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASEDATE,
            MovieContract.MovieEntry.COLUMN_POSTERPATH,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_VOTEAVG,
            MovieContract.MovieEntry.COLUMN_FAVORITE,
            MovieContract.MovieEntry.COLUMN_VIDEOKEYS
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_MOV_ID=0;
    static final int COL_MOVIE_ID =1;
    static final int COL_MOVIE_BACK_PATH =2;
    static final int COL_MOVIE_OTITLE =3;
    static final int COL_MOVIE_OVERVIEW=4;
    static final int COL_MOVIE_RELEASEDATE =5;
    static final int COL_MOVIE_POSTER_PATH=6;
    static final int COL_MOVIE_POPULARITY=7;
    static final int COL_MOVIE_VOTEAVG=8;
    static final int COL_MOVIE_FAVORITE=9;
    static final int COL_MOVIE_VIDEOKEYS=10;


    public static final String[] REVIEW_COLUMNS = {
            MovieContract.ReviewEntry._ID,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT
    };

    // These indices are tied to REVIEW_COLUMNS.  If REVIEW_COLUMNS changes, these
    // must change.
    static final int COL_REV_ID=0;
    static final int COL_REVIEW_ID =1;
    static final int COL_REVIEW_AUTHOR =2;
    static final int COL_REVIEW_CONTENT =3;

}
