package com.skf.bingeflix.Adapters;

import android.widget.ImageView;

import com.skf.bingeflix.Models.Movie;

public interface MovieItemClickListener {

    void onMovieClick(Movie movie, ImageView movieImageView); // we will need the imageview to make the shared animation between the two activity

}
