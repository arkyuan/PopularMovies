package app.com.ark.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements MovieFragment.Callback{

    private String mSort;
    private static String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSort = Utility.getPreferredSort(this);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_detail_container)!=null){
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String sort = Utility.getPreferredSort(this);
        if(sort!=null && !sort.equals(mSort)){
            MovieFragment ff = (MovieFragment)getSupportFragmentManager().findFragmentById(R.id.frag_movie);
            if(null!=ff) {
                ff.restartLoader();
            }
            MovieDetailFragment df = (MovieDetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
            if ( null != df&&mUri!=null) {
                df.onSortChanged();
            }
            mSort=sort;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri movieUri) {

        mUri = movieUri;
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_URI, movieUri);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,fragment,DETAILFRAGMENT_TAG)
                    .commit();
        }else {
            if (movieUri != null) {
                Intent intent = new Intent(this, MovieDetailActivity.class)
                        .setData(movieUri);
                startActivity(intent);
            }
        }
    }
}
