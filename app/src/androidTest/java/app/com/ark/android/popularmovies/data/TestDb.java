package app.com.ark.android.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

/**
 * Created by ark on 8/10/2015.
 */
public class TestDb extends AndroidTestCase{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
    This function gets called before each test is executed to delete the database.  This makes
    sure that we always have a clean test.
 */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Movie
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.
        Note that this only tests that the Movie table has the correct columns, since we
        give you the code for the movie table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.ReviewEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the Movie entry
        // and movie entry tables
        assertTrue("Error: Your database was created without both the movie entry and review entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
                null);



        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());


        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_BACKPATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OTITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASEDATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTERPATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POPULARITY);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTEAVG);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_FAVORITE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VIDEOKEYS);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
            Log.i(LOG_TAG, "TEST WHY!" + c.getString(columnNameIndex));
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required Movie
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                movieColumnHashSet.isEmpty());



        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.ReviewEntry.TABLE_NAME + ")",
                null);



        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> reviewColumnHashSet = new HashSet<String>();
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_MOVIE_ID);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_CONTENT);

        columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            reviewColumnHashSet.remove(columnName);
            Log.i(LOG_TAG, "TEST WHY!" + c.getString(columnNameIndex));
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required Movie
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                reviewColumnHashSet.isEmpty());

        db.close();
    }

    public long testMovieTable(){
        // First step: Get reference to writable database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleMovieValues if you wish)
        ContentValues testValues = TestUtilities.createMovieValues();

        // Insert ContentValues into database and get a row ID back
        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Movie Values", movieRowId != -1);

        // Query the database and receive a Cursor back
        Cursor c = db.query(
                MovieContract.MovieEntry.TABLE_NAME,//Table to Query
                null,//all columns
                null,// Columns for the "where"clause
                null,// values for the "where" clause
                null,// columns to group by
                null,// columns to filter by row groups
                null// sort order
        );

        // Move the cursor to a valid database row
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed",c,testValues);
        assertFalse("Error: More than one record returned from movie query", c.moveToNext());
        // Finally, close the cursor and database
        c.close();
        db.close();
        return movieRowId;
    }


    public long testReviewTable(){
        // First step: Get reference to writable database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleMovieValues if you wish)
        ContentValues testValues = TestUtilities.createReviewValues();

        // Insert ContentValues into database and get a row ID back
        long reviewRowId;
        reviewRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert North Pole Movie Values", reviewRowId != -1);

        // Query the database and receive a Cursor back
        Cursor c = db.query(
                MovieContract.ReviewEntry.TABLE_NAME,//Table to Query
                null,//all columns
                null,// Columns for the "where"clause
                null,// values for the "where" clause
                null,// columns to group by
                null,// columns to filter by row groups
                null// sort order
        );

        // Move the cursor to a valid database row
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("Error: Review Query Validation Failed",c,testValues);
        assertFalse("Error: More than one record returned from review query", c.moveToNext());
        // Finally, close the cursor and database
        c.close();
        db.close();
        return reviewRowId;
    }

}
