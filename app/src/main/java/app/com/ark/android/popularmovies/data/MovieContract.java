package app.com.ark.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ark on 8/10/2015.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "app.com.ark.android.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://app.com.ark.android.popularmovies/movie/ is a valid path for
    // looking at movie data. content://app.com.ark.android.popularmovies/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEWS = "review";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "movie";

        //Column movie id
        public static final String COLUMN_MOVIE_ID = "MovieId";

        //Column backdrop full path
        public static final String COLUMN_BACKPATH = "Backdrop_path";

        //Column original title
        public static final String COLUMN_OTITLE = "OTitle";

        //Column Overview
        public static final String COLUMN_OVERVIEW = "Overview";

        //Column Release Date
        public static final String COLUMN_RELEASEDATE = "ReleaseDate";

        //Column Poster full path
        public static final String COLUMN_POSTERPATH = "Poster_path";

        //Column Popularity
        public static final String COLUMN_POPULARITY = "popularity";

        //Column Movie Rate
        public static final String COLUMN_VOTEAVG = "VoteAvg";

        //Column is Favorite
        public static final String COLUMN_FAVORITE = "Favorite";

        //Column Video keys
        public static final String COLUMN_VIDEOKEYS = "VideoKeys";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildMovieId(String movieid){
            return CONTENT_URI.buildUpon().appendPath(movieid).build();
        }

    }


    /* Inner class that defines the table contents of the review table */
    public static final class ReviewEntry implements BaseColumns{
        public static final String TABLE_NAME = "review";

        //Column movie id
        public static final String COLUMN_MOVIE_ID = "MovieId";

        //Column backdrop full path
        public static final String COLUMN_AUTHOR = "author";

        //Column original title
        public static final String COLUMN_CONTENT = "content";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildReviewwithMovieId(String movieid){
            return CONTENT_URI.buildUpon().appendPath(movieid).build();
        }

    }


}
