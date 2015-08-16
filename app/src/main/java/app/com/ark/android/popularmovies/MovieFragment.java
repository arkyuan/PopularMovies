package app.com.ark.android.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import app.com.ark.android.popularmovies.data.MovieContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {
    private MovieCursorAdapter mMovieAdapter;
    private static final int MOVIE_LOADER = 0;
    String mSort = "null";
    FetchMovieTask movieTask;
    FetchVRTask mVRTask;
    CountDownTimer mCountDown;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("sort", mSort);
        super.onSaveInstanceState(outState);
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mMovieAdapter = new MovieCursorAdapter(getActivity(), null, 0);

        movie_entry.setAdapter(mMovieAdapter);
        movieTask = new FetchMovieTask(getActivity());
        mVRTask = new FetchVRTask((getActivity()));

        movie_entry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String movieID = cursor.getString(Constant.COL_MOVIE_ID);
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieId(movieID));
                }
            }
        });
        mCountDown= new CountDownTimer(10000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    //display in long period of time
                    Toast.makeText(getActivity(), "Please Check your Internet Connectivity", Toast.LENGTH_LONG).show();
                }
            }
        };
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortSetting = Utility.getPreferredSort(getActivity());
        // Sort order:  Descending, by popularity or average vote.
        String sortOrder;
        String selection = null;
        String[] selctionArgs=null;
        if(sortSetting.equals(MovieContract.MovieEntry.COLUMN_FAVORITE)){
            sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
            selection = MovieContract.MovieEntry.COLUMN_FAVORITE + " = ?";
            selctionArgs= new String[]{"1"};
        }
        else if(sortSetting.equals(MovieContract.MovieEntry.COLUMN_VOTEAVG)){
            sortOrder = MovieContract.MovieEntry.COLUMN_VOTEAVG + " DESC";
        }else{
            sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
        }


        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                Constant.MOVIE_COLUMNS,
                selection,
                selctionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor_data) {
        String sortSetting = Utility.getPreferredSort(getActivity());

        if(cursor_data.moveToFirst()||sortSetting.equals(MovieContract.MovieEntry.COLUMN_FAVORITE)) {
            mMovieAdapter.swapCursor(cursor_data);
            mSwipeRefreshLayout.setRefreshing(false);
        } else{
            onMovieChanged();
       }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void restartLoader(){
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }

    public void onMovieChanged(){
        if((movieTask.getStatus()!= AsyncTask.Status.RUNNING&&mVRTask.getStatus()!=AsyncTask.Status.RUNNING)) {
            movieTask= new FetchMovieTask(getActivity());
            movieTask.execute(); // Fetch Movie Async Task
            mVRTask = new FetchVRTask(getActivity());
            mVRTask.execute();//Fetch Video and Reviews Async Task
            restartLoader();
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        mCountDown.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCountDown.start();
    }

    @Override
    public void onRefresh() {
        restartLoader();
        mCountDown.start();

    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(Uri movieUri);
    }

}
