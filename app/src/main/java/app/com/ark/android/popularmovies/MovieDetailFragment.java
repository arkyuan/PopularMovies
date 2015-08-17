package app.com.ark.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import app.com.ark.android.popularmovies.data.MovieContract;

/**
 * Created by ark on 8/13/2015.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private Uri mUri;
    private static final int DETAIL_LOADER =0;
    private static final int REVIEW_LOADER =1;
    static final String DETAIL_URI = "URI";
    private static final String TRAILER_SHARE_HASHTAG = " #PopularMovieApp";
    private String mTrailerLink;
    private int mIsFavorite=0;
    private ShareActionProvider mShareActionProvider;

    private ImageView PosterImage;
    private ImageView BackPosterImage;
    private TextView OriginalTitleText;
    private TextView OverViewText;
    private TextView VoteAvgText;
    private TextView ReleaseDateText;
    private Button BtnFavorite;
    private ArrayAdapter<String> mTrailerAdapter;

    public MovieDetailFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments!=null){
            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
        }
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        PosterImage = (ImageView) rootView.findViewById(R.id.title_poster);
        BackPosterImage = (ImageView) rootView.findViewById(R.id.background_poster);
        OriginalTitleText = (TextView) rootView.findViewById(R.id.orig_title);
        OverViewText = (TextView) rootView.findViewById(R.id.summary);
        VoteAvgText = (TextView) rootView.findViewById(R.id.scores);
        ReleaseDateText = (TextView) rootView.findViewById(R.id.rel_date);
        BtnFavorite = (Button) rootView.findViewById(R.id.btn_favorite);
        BtnFavorite.setEnabled(true);

        BtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFavorite == 1) {
                    mIsFavorite = 0;
                } else {
                    mIsFavorite = 1;
                }
                BtnFavorite.setText(getBtnText(mIsFavorite));

                ContentValues updatedValues = new ContentValues();
                updatedValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, mIsFavorite);
                getActivity().getContentResolver().update(
                        mUri, updatedValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?",
                        new String[]{mUri.getLastPathSegment()});

            }
        });

        mTrailerAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_trailer,
                R.id.trailer_textview,
                new ArrayList<String>()
        );
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        switch (id) {
            case DETAIL_LOADER: {
                //Now create and return a CursorLoader tha will take care of
                //creating a Cursor for the data being displayed
                if (mUri != null) {
                    return new CursorLoader(
                            getActivity(),
                            mUri,
                            Constant.MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                }
                break;
            }

            case REVIEW_LOADER:{
                //Now create and return a CursorLoader tha will take care of
                //creating a Cursor for the data being displayed
                if (mUri != null) {
                    String movieId = MovieContract.MovieEntry.getMovieIdFromUri(mUri);
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.ReviewEntry.buildReviewwithMovieId(movieId),
                            Constant.REVIEW_COLUMNS,
                            null,
                            null,
                            null
                    );
                }
                break;
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        switch (loader.getId()) {
            case DETAIL_LOADER: {
                if (data != null && data.moveToFirst()) {

                    //poster
                    byte[] poster_url = data.getBlob(Constant.COL_MOVIE_POSTER_PATH);
                    ByteArrayInputStream posterimageStream = new ByteArrayInputStream(poster_url);
                    Bitmap posterImage = BitmapFactory.decodeStream(posterimageStream);
                    PosterImage.setImageBitmap(posterImage);

                    //backposter
                    // Read Movie back poster from cursor
                    byte[] bposter_url = data.getBlob(Constant.COL_MOVIE_BACK_PATH);
                    ByteArrayInputStream backimageStream = new ByteArrayInputStream(bposter_url);
                    Bitmap backImage = BitmapFactory.decodeStream(backimageStream);
                    BackPosterImage.setImageBitmap(backImage);

                    //Original Title
                    String otitle = data.getString(Constant.COL_MOVIE_OTITLE);
                    OriginalTitleText.setText(otitle);

                    //Overview
                    String overview = data.getString(Constant.COL_MOVIE_OVERVIEW);
                    OverViewText.setText("\n" + getString(R.string.summary) + "\n" + overview);

                    //Vote Average
                    double voteavg = data.getDouble(Constant.COL_MOVIE_VOTEAVG);
                    VoteAvgText.setText(getString(R.string.rate) + String.valueOf(voteavg));


                    //Release Date
                    String releasedate = data.getString(Constant.COL_MOVIE_RELEASEDATE);
                    ReleaseDateText.setText(getString(R.string.release_date) + releasedate);

                    //Favorite text
                    mIsFavorite = data.getInt(Constant.COL_MOVIE_FAVORITE);
                    BtnFavorite.setText(getBtnText(mIsFavorite));

                    //trailers
                    String trailerkeys = data.getString(Constant.COL_MOVIE_VIDEOKEYS);
                    if(trailerkeys!=null) {
                        String[] trailers = trailerkeys.split(" ");
                        mTrailerAdapter.clear();
                        mTrailerAdapter.addAll(trailers);
                    }
                    else {
                        mTrailerAdapter.clear();
                    }
                    //Pointing to the LinearLayout
                    LinearLayout trailerContainer = (LinearLayout) getActivity().findViewById(R.id.trailer_container);
                    trailerContainer.removeAllViews();
                    final int adapterCount = mTrailerAdapter.getCount();
                    //adding each adapter item to the LinearLayout
                    for (int i = 0; i < adapterCount; i++) {

                        //share the first trailer
                        if(i==0)
                        {
                            String trailer = mTrailerAdapter.getItem(0);
                            String url = "https://www.youtube.com/watch?v=" + trailer;
                            mTrailerLink = String.format("%s - %s: %s", otitle,getString(R.string.trailer1link), url);
                        }

                        View item = mTrailerAdapter.getView(i, null, null);
                        TextView title = (TextView) item.findViewById(R.id.trailer_textview);
                        title.setText(getString(R.string.trailer) + (i + 1));
                        trailerContainer.addView(item);
                        final int position = i;
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String trailer = mTrailerAdapter.getItem(position);
                                String url = "https://www.youtube.com/watch?v=" + trailer;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        });
                    }

                    // If onCreateOptionsMenu has already happened, we need to update the share intent now.
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareTrailerIntent());
                    }

                }
                break;
            }
            case REVIEW_LOADER:{
                //cursor data
                while(data.moveToNext()) {
                    //Pointing to the LinearLayout
                    LinearLayout reviewContainer = (LinearLayout) getActivity().findViewById(R.id.review_container);
                    reviewContainer.removeAllViews();
                    String author = data.getString(Constant.COL_REVIEW_AUTHOR);
                    String content = data.getString(Constant.COL_REVIEW_CONTENT);

                    // Create TextView Author
                    TextView authorTextView = new TextView(getActivity());
                    authorTextView.setTextColor(getResources().getColor(R.color.white));
                    authorTextView.setText(getString(R.string.author)+author);
                    reviewContainer.addView(authorTextView);

                    // Create TextView Content
                    TextView contentTextView = new TextView(getActivity());
                    contentTextView.setText(getString(R.string.reviews) + content);
                    contentTextView.setTextColor(getResources().getColor(R.color.orange));
                    contentTextView.setPadding(0, 30, 0, 30);
                    reviewContainer.addView(contentTextView);
                }
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mUri!=null) {
            getLoaderManager().restartLoader(REVIEW_LOADER, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void onSortChanged( ) {
        //mUri = null;
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        getLoaderManager().restartLoader(REVIEW_LOADER, null, this);
    }

    public String getBtnText(int isFavorite){
        if(isFavorite==1){
            return getString(R.string.undo_favorite);

        }else{
            return getString(R.string.favorite);
        }
    }

    private Intent createShareTrailerIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mTrailerLink + TRAILER_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailsharefrag, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
            // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mTrailerLink != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }
}
