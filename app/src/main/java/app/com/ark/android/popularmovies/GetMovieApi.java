package app.com.ark.android.popularmovies;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ark on 8/16/2015.
 */
public interface GetMovieApi {

    @GET("/3/discover/movie")
    Movie getMovies(
            @Query("sort_by") String sortOrder,
            @Query("api_key") String apiKey
    );

    @GET("/3/movie/{movieId}")
    VideoReview getVideoReviews(
            @Path("movieId") String movieId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String append
    );

}
