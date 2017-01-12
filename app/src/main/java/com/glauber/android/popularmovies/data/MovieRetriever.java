package com.glauber.android.popularmovies.data;

import android.net.Uri;
import android.os.AsyncTask;

import com.glauber.android.popularmovies.IAsyncTaskDelegate;
import com.glauber.android.popularmovies.model.Movie;
import com.glauber.android.popularmovies.model.MovieSortOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by glauberl on 1/6/2017.
 */

public class MovieRetriever extends AsyncTask<MovieSortOrder, Void, Movie[]> {

    private IAsyncTaskDelegate<Movie[]> mTaskDelegate;

    public MovieRetriever(IAsyncTaskDelegate<Movie[]> taskDelegate) {

        this.mTaskDelegate = taskDelegate;
    }

    @Override
    protected Movie[] doInBackground(MovieSortOrder... movieSortOrders) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        try {

            MovieSortOrder order = movieSortOrders[0];

            URL url;

            if (order == MovieSortOrder.MOST_POPULAR) {
                url = new URL("https://api.themoviedb.org/3/movie/popular?api_key=a72dce21f0bb2ad441a3dd3042a51550");
            } else {
                url = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=a72dce21f0bb2ad441a3dd3042a51550");
            }

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            jsonStr = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (urlConnection != null)
                urlConnection.disconnect();

        }

        Movie[] extractedMovies = extractFromJsonString(jsonStr);

        return extractedMovies;

    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);

        if (this.mTaskDelegate != null) {
            this.mTaskDelegate.onFinished(movies);
        }
    }

    private Movie[] extractFromJsonString(String jsonStr) {

        ArrayList<Movie> movieArrayList = new ArrayList<>();

        try {
            JSONObject fullJsonObj = new JSONObject(jsonStr);

            JSONArray resultsArray = fullJsonObj.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject result = resultsArray.getJSONObject(i);

                String posterPathRaw = result.getString("poster_path");

                String posterUrl = "https://image.tmdb.org/t/p/w500" + posterPathRaw;

                String title = result.getString("title");

                String releaseDateString = result.getString("release_date");

                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                Date releaseDate = fmt.parse(releaseDateString);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(releaseDate);
                int releaseYear = calendar.get(Calendar.YEAR);

                Movie movie = new Movie();
                movie.Synopsis = result.getString("overview");
                movie.UserRating = result.getDouble("vote_average");
                movie.ReleaseYear = releaseYear;
                movie.PosterUrl = Uri.parse(posterUrl);
                movie.Title = title;
                movieArrayList.add(movie);

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return movieArrayList.toArray(new Movie[0]);

    }


}
