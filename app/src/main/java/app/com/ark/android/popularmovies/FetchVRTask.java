package app.com.ark.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import app.com.ark.android.popularmovies.data.MovieContract;

/**
 * Created by ark on 8/14/2015.
 */
public class FetchVRTask extends AsyncTask<Void,Void,Void> {

    private final String LOG_TAG = FetchVRTask.class.getSimpleName();

    private final Context mContext;

    public FetchVRTask(Context context) {
        mContext = context;
    }

    private Cursor getMoviesFromDatabase(){
        return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,Constant.MOVIE_COLUMNS,null,null,null);
    }

    private void updateVRtoTable(String Movie_id,String VRRawJsonStr)throws JSONException {
        //trailers
        final String OWM_TRAILERS = "trailers";
        final String OWM_YOUTUBE = "youtube";
        final String OWM_SOURCE = "source";

        JSONObject RVJson = new JSONObject(VRRawJsonStr);

        //trailers
        JSONObject TrailersObj = RVJson.getJSONObject(OWM_TRAILERS);
        JSONArray YoutubeArray = TrailersObj.getJSONArray(OWM_YOUTUBE);

        String trailerkeys = null;

        for(int i=0; i<YoutubeArray.length();i++){
            JSONObject yotube = YoutubeArray.getJSONObject(i);
            String source = yotube.getString(OWM_SOURCE);

            //concatenate the keys by space
            if(trailerkeys==null){
                trailerkeys = source;
            } else {
                trailerkeys = trailerkeys + " " + source;
            }
        }

        if(trailerkeys!=null){
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(MovieContract.MovieEntry.COLUMN_VIDEOKEYS, trailerkeys);
            mContext.getContentResolver().update(MovieContract.MovieEntry.buildMovieId(Movie_id),updatedValues,MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?",
                    new String[] {Movie_id});
        }


        //reviews
        final String OWM_REVIEWS = "reviews";
        final String OWM_RESULTS = "results";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT = "content";

        //reviews
        JSONObject ReviewObj = RVJson.getJSONObject(OWM_REVIEWS);
        JSONArray ResultsArray = ReviewObj.getJSONArray(OWM_RESULTS);

        for(int i=0; i<ResultsArray.length();i++){
            JSONObject review = ResultsArray.getJSONObject(i);

            String author = review.getString(OWM_AUTHOR);
            String content = review.getString(OWM_CONTENT);

            ContentValues insertValues = new ContentValues();
            insertValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,Movie_id);
            insertValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,author);
            insertValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT,content);

            mContext.getContentResolver().insert(MovieContract.ReviewEntry.CONTENT_URI,insertValues);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {

        Cursor c = getMoviesFromDatabase();

        while(c.moveToNext()) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //Read the input stream into a String
            InputStream inputStream = null;

            //raw JSON response as a string from movie API
            String VRJsonRawString;

            //API Key to access the database
            String APIKey = "fdb22fc875d6820fff402852faf29bc8";

            //Append
            String Append = "trailers,reviews";

            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/"+c.getString(Constant.COL_MOVIE_ID)+"?";
                final String APPEND_PARAM = "append_to_response";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPEND_PARAM, Append)
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
                VRJsonRawString = buffer.toString();
                updateVRtoTable(c.getString(Constant.COL_MOVIE_ID),VRJsonRawString);

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
        }
        c.close();
        return null;
    }
}
