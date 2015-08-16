package app.com.ark.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import app.com.ark.android.popularmovies.utils.PollingCheck;

/**
 * Created by ark on 8/10/2015.
 */
public class TestUtilities extends AndroidTestCase {
    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            if(idx==7){
                assertEquals("Value '" + entry.getValue().toString() +
                        "' did not match the expected value '" +
                        expectedValue + "'. " + error, expectedValue, Double.toString(valueCursor.getDouble(idx)));
            }else {
                assertEquals("Value '" + entry.getValue().toString() +
                        "' did not match the expected value '" +
                        expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
            }
        }
    }

    /*
    Students: You can uncomment this helper function once you have finished creating the
    MovieEntry part of the MovieContract.
 */
    static ContentValues createMovieValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "102899");
        testValues.put(MovieContract.MovieEntry.COLUMN_BACKPATH, "http://image.tmdb.org/t/p/w500/kvXLZqY0Ngl1XSw7EaMQO0C1CCj.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_OTITLE, "Ant-Man");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Armed with the astonishing ability to shrink in scale but increase in strength, con-man Scott Lang must embrace his inner-hero and help his mentor, Dr. Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a heist that will save the world.");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE, "2015-07-17");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTERPATH, "http://image.tmdb.org/t/p/w185/7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg");
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 0.001882);
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTEAVG, 7.1);
        testValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 0);
        testValues.put(MovieContract.MovieEntry.COLUMN_VIDEOKEYS, "xInh3VhAWs8 pWdKf3MneyI");

        return testValues;
    }

    static ContentValues createReviewValues(){
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, "102899");
        testValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,"Kenechukwu");
        testValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT,"Revenge is so so cold...... action packed, thrilling and suspense filled but still some bits of make believe tricks here and there. \\r\\nHow did Hobbs know that there's a need for the drone to be brought down right after leaving the hospital bed? how did he know where the drone was at the moment when he left with the ambulance? How did Deckard Shaw know both that Toretto and his crew will be both on the route of Afghanistan and also when they were at Abu Dhabi? \\r\\nIts a little bit odd and out of place for the director and script editor.... \\r\\nAs for the next upcoming part in the franchise, i think Mark Wahlberg should be considered for a part in the rest of Fast and furious franchise..... Cheers ! ! !");

        return testValues;
    }


    static long insertMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();

        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Values", movieRowId != -1);

        return movieRowId;
    }


    /*
    Students: The functions we provide inside of TestProvider use this utility class to test
    the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
    CTS tests.
    Note that this only tests that the onChange function is called; it does not test that the
    correct Uri is returned.
 */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
