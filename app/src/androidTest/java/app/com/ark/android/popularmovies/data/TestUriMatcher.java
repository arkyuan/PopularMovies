package app.com.ark.android.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by ark on 8/11/2015.
 */
public class TestUriMatcher extends AndroidTestCase {

    public static final String LOG_TAG = TestUriMatcher.class.getSimpleName();

    private static final String MOVIE_QUERY = "140046";
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_ID_DIR = MovieContract.MovieEntry.buildMovieId(MOVIE_QUERY);
    private static final Uri TEST_REVIEW_DIR = MovieContract.ReviewEntry.CONTENT_URI;
    private static final Uri TEST_REVIEW_WITH_MID_DIR = MovieContract.ReviewEntry.buildReviewwithMovieId(MOVIE_QUERY);

    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);
        assertEquals("Error: The MOVIE WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_WITH_ID_DIR), MovieProvider.MOVIE_WITH_ID);
        assertEquals("Error: The REVIEW URI was matched incorrectly.",
                testMatcher.match(TEST_REVIEW_DIR), MovieProvider.REVIEW);
        assertEquals("Error: The REVIEW WITH ID URI was matched incorrectly.",
                testMatcher.match(TEST_REVIEW_WITH_MID_DIR), MovieProvider.REVIEW_WITH_MID);

        //Log.v(LOG_TAG, String.valueOf(TEST_MOVIE_WITH_ID_DIR));

    }
}
