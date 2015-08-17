package app.com.ark.android.popularmovies;

import java.util.List;

/**
 * Created by ark on 8/16/2015.
 */
public class VideoReview {

    public Trailerset trailers;
    public Reviewset reviews;

    public class Trailerset {
        List<Youtubeset> youtube;
    }

    public class Reviewset {
        List<Resultset> results;
    }

    public class Youtubeset{
        String source;
    }

    public class Resultset{
        String author;
        String content;
    }
}
