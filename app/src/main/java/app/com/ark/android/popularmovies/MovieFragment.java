package app.com.ark.android.popularmovies;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private MovieAdapterArrary mMovieAdapter;
    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        GridView movie_entry = (GridView) rootView.findViewById(R.id.gridview_movie);

        mMovieAdapter = new MovieAdapterArrary(
                getActivity(),
                R.layout.list_item_movie,
                R.id.list_item_movie_imageview,
                new ArrayList<Movie>()
        );

        movie_entry.setAdapter(mMovieAdapter);
        movie_entry.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("original_title",movie.getmOTitle())
                        .putExtra("poster_path",movie.getmPoster_path())
                        .putExtra("overview",movie.getmOverview())
                        .putExtra("vote_average",movie.getmVoteAvg())
                        .putExtra("release_date",movie.getmReleaseDate())
                        .putExtra("backdrop_path",movie.getmBackdrop_path());
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String,Void,Movie[]> {
        //private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        private Movie[] getMovieDataFromJson(String movieJsonRawStr) throws JSONException{

            final String OWM_RESULTS = "results";
            final String OWM_MOVIEID = "id";
            final String OWM_TITLE ="title";
            final String OWM_OTITLE ="original_title";
            final String OWM_RELEASE_DATE ="release_date";
            final String OWN_POSTER ="backdrop_path";
            final String OWN_VOTEAVG = "vote_average";
            final String OWN_SUMMARY ="overview";
            final String OWM_PATH ="poster_path";
            final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
            final String IMAGE_SIZE = "w185";
            final String IMAGE_SIZEORI = "original";
            JSONObject movieJson = new JSONObject(movieJsonRawStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);
            Movie[] resultList = new Movie[movieArray.length()];

            for(int i =0; i<movieArray.length();i++){
                JSONObject movie = movieArray.getJSONObject(i);
                resultList[i] = new Movie();
                resultList[i].setId(movie.getString(OWM_MOVIEID));
                resultList[i].setmTitle(movie.getString(OWM_TITLE));
                resultList[i].setmOTitle(movie.getString(OWM_OTITLE));
                resultList[i].setmReleaseDate(movie.getString(OWM_RELEASE_DATE));
                resultList[i].setmBackdrop_path(IMAGE_BASE_URL+IMAGE_SIZEORI+movie.getString(OWN_POSTER));
                resultList[i].setmVoteAvg(movie.getString(OWN_VOTEAVG));
                resultList[i].setmOverview(movie.getString(OWN_SUMMARY));
                resultList[i].setmPoster_path(IMAGE_BASE_URL+IMAGE_SIZE+movie.getString(OWM_PATH));
            }

            //for (String s : resultStrs) {
            //    Log.v(LOG_TAG, "Movie URL: " + s);
            //}

            return resultList;
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //raw JSON response as a string from movie API
            String movieJsonRawString = null;

            //API Key to access the database
            String APIKey = "";

            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, APIKey)
                        .build();

                URL url = new URL(builtUri.toString());
                //Log.v(LOG_TAG, "Built URL " + builtUri.toString());

                //create the request to Movie API and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
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
                //Log.v(LOG_TAG, "Movie JSON String: " + movieJsonRawString);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Error", e);
                return null;
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

                try{
                    return getMovieDataFromJson(movieJsonRawString);
                } catch (JSONException e){
                    e.printStackTrace();
                }

                return null;
            }


        }


        @Override
        protected void onPostExecute(Movie[] result) {
            if(result!=null){
                mMovieAdapter.clear();
                mMovieAdapter.addAll(result);
            }
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        updateMovie();
    }

    private void updateMovie() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = sharedPref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        new FetchMovieTask().execute(sort);
    }
}
