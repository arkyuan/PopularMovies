package app.com.ark.android.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by ark on 8/10/2015.
 */
public class TestMovieContract extends AndroidTestCase{
    private static final String MOVIE_QUERY = "140046";
    public void testBuildMovieId() {
        Uri movieUri = MovieContract.MovieEntry.buildMovieId(MOVIE_QUERY);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildMovieId in " +
                        "MovieContract.",
                movieUri);
        assertEquals("Error: movie id not properly appended to the end of the Uri",
                MOVIE_QUERY, movieUri.getLastPathSegment());
        assertEquals("Error: movie id Uri doesn't match our expected result",
                movieUri.toString(),
                "content://app.com.ark.android.popularmovies/movie/140046");
    }

    public void testBuildMovieIdReview() {
        Uri reviewUri = MovieContract.ReviewEntry.buildReviewwithMovieId(MOVIE_QUERY);
        assertNotNull("Error: Null Uri returned.  You must fill-in buildReviewwithMovieId in " +
                        "MovieContract.",
                reviewUri);
        assertEquals("Error: movie id not properly appended to the end of the Uri",
                MOVIE_QUERY, reviewUri.getLastPathSegment());
        assertEquals("Error: movie id Uri doesn't match our expected result",
                reviewUri.toString(),
                "content://app.com.ark.android.popularmovies/review/140046");
    }

}
