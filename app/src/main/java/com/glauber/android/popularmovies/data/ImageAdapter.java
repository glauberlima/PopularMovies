package com.glauber.android.popularmovies.data;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.glauber.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;


/**
 * Created by glauberl on 1/5/2017.
 */

public class ImageAdapter extends BaseAdapter {

    private final Context _context;
    private final Movie[] _movies;

    public ImageAdapter(Context context, Movie[] movies) {
        _context = context;
        _movies = movies;
    }

    @Override
    public int getCount() {
        return this._movies.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ImageView imageView;
        //final View view;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(this._context);
            imageView.setAdjustViewBounds(true);
            //view = LayoutInflater.from(this.context).inflate(R.layout.movie_poster, parent, false);
            //ImageView imageView = (ImageView) view.findViewById(R.id.movie_poster_item);

        } else {
            imageView = (ImageView) convertView;
        }

        Uri moviePoster = _movies[position].PosterUrl;
        Picasso.with(this._context).setIndicatorsEnabled(true);
        Picasso.with(this._context)
                .load(moviePoster)
                .into(imageView);

        return imageView;
    }
}