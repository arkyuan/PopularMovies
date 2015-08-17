package app.com.ark.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import app.com.ark.android.popularmovies.data.MovieContract;
import retrofit.RestAdapter;

/**
 * Created by ark on 8/14/2015.
 */
public class FetchVRTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG = FetchVRTask.class.getSimpleName();

    //Base URL
    private static final String API_URL = "http://api.themoviedb.org";

    //API Key to access the database
    private static final String APIKey = "fdb22fc875d6820fff402852faf29bc8";
    //Append
    private static final String Append = "trailers,reviews";
    private final Context mContext;
    RestAdapter mRestAdapter;

    public FetchVRTask(Context context) {
        mContext = context;
    }

    private Cursor getMoviesFromDatabase(){
        return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,Constant.MOVIE_COLUMNS,null,null,null);
    }

    @Override
    protected void onPreExecute() {
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
    }

    private void updateVRtoTable(String movieId,VideoReview JVRs){

        String trailerkeys = null;
        for(VideoReview.Youtubeset youtube : JVRs.trailers.youtube){
            //concatenate the keys by space
            if(trailerkeys==null){
                trailerkeys = youtube.source;
            } else {
                trailerkeys = trailerkeys + " " + youtube.source;
            }
        }

        if(trailerkeys!=null){
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(MovieContract.MovieEntry.COLUMN_VIDEOKEYS, trailerkeys);
            mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieId(movieId),updatedValues,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?",
                    new String[] {movieId});
        }


        for(VideoReview.Resultset result : JVRs.reviews.results){

            ContentValues insertValues = new ContentValues();
            insertValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,movieId);
            insertValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,result.author);
            insertValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT,result.content);

            mContext.getContentResolver().insert(MovieContract.ReviewEntry.CONTENT_URI,insertValues);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        Cursor c = getMoviesFromDatabase();

        while(c.moveToNext()) {
            VideoReview VRs=null;
            //create the request to Movie API and open the connection
            if(Utility.checkInternetConnection(mContext)){
                GetMovieApi methods = mRestAdapter.create(GetMovieApi.class);
                VRs= methods.getVideoReviews(c.getString(Constant.COL_MOVIE_ID), APIKey,Append);
            } else {
                cancel(true);
            }

            if(VRs!=null) {
                updateVRtoTable(c.getString(Constant.COL_MOVIE_ID),VRs);
            }
        }
        c.close();
        return null;
    }
}
