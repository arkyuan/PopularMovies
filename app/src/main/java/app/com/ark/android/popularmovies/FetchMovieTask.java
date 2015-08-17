package app.com.ark.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import app.com.ark.android.popularmovies.data.MovieContract.MovieEntry;
import retrofit.RestAdapter;

/**
 * Created by ark on 8/11/2015.
 */
public class FetchMovieTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    //Base URL
    private static final String API_URL = "http://api.themoviedb.org";
    //API Key to access the database
    private static final String APIKey = "fdb22fc875d6820fff402852faf29bc8";
    //sort_by
    private static final String SortBy = "popularity.desc";

    private final Context mContext;

    RestAdapter mRestAdapter;
    public FetchMovieTask(Context context) {
        mContext = context;
    }


    @Override
    protected void onPreExecute() {
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
    }

    private byte[] getLogoImage(String url){
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
            return null;
        }

    }

    private void getMovieDataFromJsonToTable(Movie JMovies){


        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";
        final String IMAGE_SIZEORI = "w500";

        // Insert the new movie information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(JMovies.results.size());

        for(Movie.Results results : JMovies.results){
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, results.id);
            movieValues.put(MovieEntry.COLUMN_OTITLE, results.original_title);
            movieValues.put(MovieEntry.COLUMN_OVERVIEW, results.overview);
            movieValues.put(MovieEntry.COLUMN_RELEASEDATE, results.release_date);
            movieValues.put(MovieEntry.COLUMN_POPULARITY, results.popularity);
            movieValues.put(MovieEntry.COLUMN_VOTEAVG, results.vote_average);
            movieValues.put(MovieEntry.COLUMN_FAVORITE, 0);

            //Fetch pictures
            byte[] posteredata = getLogoImage(IMAGE_BASE_URL + IMAGE_SIZE + results.poster_path);
            byte[] backdropdata = getLogoImage(IMAGE_BASE_URL + IMAGE_SIZEORI + results.backdrop_path);
            movieValues.put(MovieEntry.COLUMN_POSTERPATH, posteredata);
            movieValues.put(MovieEntry.COLUMN_BACKPATH, backdropdata);

            cVVector.add(movieValues);
        }


        int inserted = 0;
        // add to database
        if ( cVVector.size() > 0 ) {
            // call bulkInsert to add the movieEntries to the database here
            ContentValues[] cvArrary = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArrary);
            inserted = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI,cvArrary);
        }
        Log.d(LOG_TAG, "FetchMovieTask Complete. " + inserted + " Inserted");
    }

    @Override
    protected Void doInBackground(Void... params) {
        Movie movies = null;
        //create the request to Movie API and open the connection
        if(Utility.checkInternetConnection(mContext)){
            GetMovieApi methods = mRestAdapter.create(GetMovieApi.class);
            movies= methods.getMovies(SortBy,APIKey);
        } else {
            cancel(true);
        }

        if(movies!=null) {
            getMovieDataFromJsonToTable(movies);
        }
            //Log.v(LOG_TAG, "Movie JSON String: " + movieJsonRawString);

        return null;
    }
}