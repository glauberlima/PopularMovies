package com.glauber.android.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.glauber.android.popularmovies.MovieParcelable;
import com.glauber.android.popularmovies.R;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        MovieParcelable movieParcelable = getIntent().getParcelableExtra(MovieParcelable.NAME);
        String posterUrl = movieParcelable.getPosterUrl();
        String releaseYearString = String.valueOf(movieParcelable.getReleaseYear());
        String synopsis = movieParcelable.getSynopsis();
        String title = movieParcelable.getTitle();
        Double userRating = movieParcelable.getUserRating();

        ImageView moviePoster = (ImageView) findViewById(R.id.movie_poster);
        Picasso.with(this).setIndicatorsEnabled(true);
        Picasso.with(this)
                .load(posterUrl)
                .into(moviePoster);

        this.setTitle(title + " (" + releaseYearString + ")");

        TextView releaseDateView = (TextView) findViewById(R.id.movie_release_date);
        releaseDateView.setText(releaseYearString);

        TextView synopsisView = (TextView) findViewById(R.id.movie_synopsis);
        synopsisView.setText(synopsis);

        TextView userRatingView = (TextView) findViewById(R.id.movie_user_rating);
        userRatingView.setText(userRating.toString());

    }
}
