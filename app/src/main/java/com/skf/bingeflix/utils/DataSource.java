package com.skf.bingeflix.utils;

import com.skf.bingeflix.Models.Movie;
import com.skf.bingeflix.R;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static List<Movie> getpopularMovies() {
        List<Movie> lstMovies = new ArrayList<>();
        lstMovies.add(new Movie("Moana", R.drawable.moana, R.drawable.spidercover));
        lstMovies.add(new Movie("The Martian", R.drawable.themartian, R.drawable.superman));
        lstMovies.add(new Movie("Black Panther", R.drawable.blackp, R.drawable.avengers));
        lstMovies.add(new Movie("The Martian", R.drawable.themartian, R.drawable.superman));
        lstMovies.add(new Movie("Moana", R.drawable.moana, R.drawable.spidercover));
        lstMovies.add(new Movie("Black Panther", R.drawable.blackp, R.drawable.kgf));
        return lstMovies;

    }

    public static List<Movie> getlanguageMovies() {
        List<Movie> lstMovies = new ArrayList<>();
        lstMovies.add(new Movie("The Martian", R.drawable.themartian, R.drawable.superman));
        lstMovies.add(new Movie("Moana", R.drawable.moana, R.drawable.spidercover));
        lstMovies.add(new Movie("Black Panther", R.drawable.blackp, R.drawable.avengers));
        lstMovies.add(new Movie("Moana", R.drawable.moana, R.drawable.spidercover));
        lstMovies.add(new Movie("The Martian", R.drawable.themartian, R.drawable.batman));
        lstMovies.add(new Movie("Black Panther", R.drawable.blackp, R.drawable.kgf));
        return lstMovies;

    }
}
