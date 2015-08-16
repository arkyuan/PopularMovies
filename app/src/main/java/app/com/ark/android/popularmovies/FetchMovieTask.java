package app.com.ark.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import app.com.ark.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by ark on 8/11/2015.
 */
public class FetchMovieTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    public FetchMovieTask(Context context) {
        mContext = context;
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

    private void getMovieDataFromJsonToTable(String movieJsonRawStr) throws JSONException{

        final String OWM_RESULTS = "results";
        final String OWM_MOVIEID = "id";
        final String OWN_POSTER ="backdrop_path";
        final String OWM_OTITLE ="original_title";
        final String OWN_SUMMARY ="overview";
        final String OWM_RELEASE_DATE ="release_date";
        final String OWM_PATH ="poster_path";
        final String OWM_POPULARITY = "popularity";
        final String OWN_VOTEAVG = "vote_average";


        final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";
        final String IMAGE_SIZEORI = "w500";
        JSONObject movieJson = new JSONObject(movieJsonRawStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

        // Insert the new movie information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

        for(int i =0; i<movieArray.length();i++){
            String movieId;
            String backdrop_path;
            String oTitle;
            String overview;
            String releasedate;
            String poster_path;
            double popularity;
            double voteAvg;

            JSONObject movie = movieArray.getJSONObject(i);

            ContentValues movieValues = new ContentValues();

            movieId = movie.getString(OWM_MOVIEID);
            backdrop_path = IMAGE_BASE_URL + IMAGE_SIZEORI + movie.getString(OWN_POSTER);
            oTitle = movie.getString(OWM_OTITLE);
            overview = movie.getString(OWN_SUMMARY);
            releasedate = movie.getString(OWM_RELEASE_DATE);
            poster_path = IMAGE_BASE_URL + IMAGE_SIZE + movie.getString(OWM_PATH);
            popularity = movie.getDouble(OWM_POPULARITY);
            voteAvg = movie.getDouble(OWN_VOTEAVG);

            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieId);

            movieValues.put(MovieEntry.COLUMN_OTITLE, oTitle);
            movieValues.put(MovieEntry.COLUMN_OVERVIEW, overview);
            movieValues.put(MovieEntry.COLUMN_RELEASEDATE, releasedate);

            movieValues.put(MovieEntry.COLUMN_POPULARITY, popularity);
            movieValues.put(MovieEntry.COLUMN_VOTEAVG, voteAvg);
            movieValues.put(MovieEntry.COLUMN_FAVORITE, 0);



            byte[] posteredata = getLogoImage(poster_path);
            byte[] backdropdata = getLogoImage(backdrop_path);
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

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Read the input stream into a String
        InputStream inputStream =null;

        //raw JSON response as a string from movie API
        String movieJsonRawString;

        //API Key to access the database
        String APIKey = "fdb22fc875d6820fff402852faf29bc8";

        //sort_by
        String SortBy = "popularity.desc";

        try {
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY_PARAM, SortBy)
                    .appendQueryParameter(API_KEY_PARAM, APIKey)
                    .build();

            URL url = new URL(builtUri.toString());
            //Log.v(LOG_TAG, "Built URL " + builtUri.toString());

            //create the request to Movie API and open the connection
            if(Utility.checkInternetConnection(mContext)){
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //Read the input stream into a String
                inputStream = urlConnection.getInputStream();
            } else {
                cancel(true);
            }


            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                //Nothing
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                //buffer for debugging
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                //Stream was empty
                return null;
            }

            movieJsonRawString = buffer.toString();
            getMovieDataFromJsonToTable(movieJsonRawString);
            //Log.v(LOG_TAG, "Movie JSON String: " + movieJsonRawString);
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error", e);
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    //Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }
}