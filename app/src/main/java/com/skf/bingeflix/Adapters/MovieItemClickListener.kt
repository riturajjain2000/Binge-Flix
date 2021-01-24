package com.skf.bingeflix.Adapters

import android.widget.ImageView
import com.skf.bingeflix.Models.Movie

interface MovieItemClickListener {
    fun onMovieClick(movie: Movie?, movieImageView: ImageView?) // we will need the imageview to make the shared animation between the two activity
}