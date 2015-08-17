package app.com.ark.android.popularmovies;

import java.util.List;

/**
 * Created by ark on 8/16/2015.
 */
public class Movie {

    public List<Results> results;

    public class Results {
        String id;
        String backdrop_path;
        String original_title;
        String overview;
        String release_date;
        String poster_path;
        String popularity;
        String vote_average;
    }

}
