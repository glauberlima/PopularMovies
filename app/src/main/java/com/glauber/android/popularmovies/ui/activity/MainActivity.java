package com.glauber.android.popularmovies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.glauber.android.popularmovies.BuildConfig;
import com.glauber.android.popularmovies.IAsyncTaskDelegate;
import com.glauber.android.popularmovies.MovieParcelable;
import com.glauber.android.popularmovies.R;
import com.glauber.android.popularmovies.business.NetworkStatus;
import com.glauber.android.popularmovies.data.ImageAdapter;
import com.glauber.android.popularmovies.data.MovieRetriever;
import com.glauber.android.popularmovies.model.Movie;
import com.glauber.android.popularmovies.model.MovieSortOrder;

public class MainActivity extends AppCompatActivity {

    private Context mContext = null;

    public MainActivity() {
        mContext = this;
    }

    private void retrieveMovies(final Context ctx) {

        boolean isConnected = NetworkStatus.isConnected(ctx);

        View view = findViewById(android.R.id.content);

        if (!isConnected) {
            Snackbar
                    .make(view, "Sem conexão com a Internet.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Tentar novamente", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            retrieveMovies(ctx);
                        }
                    })
                    .show();
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String pref_movies_sort_order = preferences.getString("pref_movies_sort_order", "");

        MovieSortOrder sortOrder;

        if (pref_movies_sort_order.equalsIgnoreCase("1")) {
            sortOrder = MovieSortOrder.MOST_POPULAR;
        } else {
            sortOrder = MovieSortOrder.TOP_RATED;
        }

        new MovieRetriever(new IAsyncTaskDelegate<Movie[]>() {
            @Override
            public void onFinished(final Movie[] movies) {
                ImageAdapter adapter = new ImageAdapter(ctx, movies);
                GridView gridView = (GridView) findViewById(R.id.gridView);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Movie m = movies[position];
                        MovieParcelable movieParcelable = new MovieParcelable();
                        movieParcelable.setPosterUrl(m.PosterUrl.toString());
                        movieParcelable.setReleaseYear(m.ReleaseYear);
                        movieParcelable.setSynopsis(m.Synopsis);
                        movieParcelable.setTitle(m.Title);
                        movieParcelable.setUserRating(m.UserRating);

                        Intent i = new Intent(mContext, DetailsActivity.class);
                        i.putExtra(MovieParcelable.NAME, movieParcelable);

                        startActivity(i);
                    }
                });
            }
        }).execute(sortOrder);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (BuildConfig.DEBUG) {

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads()
                    .permitDiskWrites()
                    .penaltyLog()
                    .penaltyDeath()
                    .penaltyDialog()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Context context = this;
        retrieveMovies(context);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        final Context context = this;
//        retrieveMovies(context);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_main_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
