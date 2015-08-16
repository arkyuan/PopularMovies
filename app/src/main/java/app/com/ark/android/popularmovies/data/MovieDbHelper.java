package app.com.ark.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.com.ark.android.popularmovies.data.MovieContract.MovieEntry;
import app.com.ark.android.popularmovies.data.MovieContract.ReviewEntry;
/**
 * Created by ark on 8/10/2015.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = MovieDbHelper.class.getSimpleName();
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                // Create a table to hold movies. A movies consists of the string supplied in the
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_BACKPATH + " BLOB NULL, " +
                MovieEntry.COLUMN_OTITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NULL, " +
                MovieEntry.COLUMN_RELEASEDATE + " TEXT NULL, " +
                MovieEntry.COLUMN_POSTERPATH + " BLOB NULL, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NULL, " +
                MovieEntry.COLUMN_VOTEAVG + " REAL NULL, " +
                MovieEntry.COLUMN_FAVORITE + " INT NOT NULL, " +
                MovieEntry.COLUMN_VIDEOKEYS + " TEXT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);


        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                // Create a table to hold movies. A movies consists of the string supplied in the
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.

        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MovieEntry.TABLE_NAME + "'");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                ReviewEntry.TABLE_NAME + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }
}
