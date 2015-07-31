package app.com.ark.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Bundle extra = getIntent().getExtras();
        Movie m = extra.getParcelable("movieData");
        if(m!=null){
            String original_title = m.getmOTitle();
            String poster_path = m.getmPoster_path();
            String overview = m.getmOverview();
            String vote_average = m.getmVoteAvg();
            String release_date = m.getmReleaseDate();
            String back_poster = m.getmBackdrop_path();

            ImageView PosterImage = (ImageView) findViewById(R.id.title_poster);
            ImageView BackPosterImage = (ImageView) findViewById(R.id.background_poster);
            TextView OriginalTitleText = (TextView) findViewById(R.id.orig_title);
            TextView OverViewText = (TextView) findViewById(R.id.summary);
            TextView VoteAvgText = (TextView) findViewById(R.id.scores);
            TextView ReleaseDateText = (TextView) findViewById(R.id.rel_date);



            OriginalTitleText.setText(original_title);
            OverViewText.setText("\n"+"Summary: "+"\n"+overview);
            VoteAvgText.setText("    Scores: "+vote_average);
            ReleaseDateText.setText("    Release Date: "+ release_date);

            Picasso.with(this).load(poster_path).error(R.drawable.movieicon).into(PosterImage);
            if(back_poster!=null){
                Picasso.with(this).load(back_poster).into(BackPosterImage);
            }
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
