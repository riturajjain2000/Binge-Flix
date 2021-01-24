package com.skf.bingeflix.utils

import com.skf.bingeflix.Models.Movie
import com.skf.bingeflix.R
import java.util.*

object DataSource {
    @JvmStatic
    fun getpopularMovies(): List<Movie> {
        val lstMovies: MutableList<Movie> = ArrayList()
        lstMovies.add(Movie("Moana", R.drawable.moana, R.drawable.spidercover))
        lstMovies.add(Movie("The Martian", R.drawable.themartian, R.drawable.superman))
        lstMovies.add(Movie("Black Panther", R.drawable.blackp, R.drawable.avengers))
        lstMovies.add(Movie("The Martian", R.drawable.themartian, R.drawable.superman))
        lstMovies.add(Movie("Moana", R.drawable.moana, R.drawable.spidercover))
        lstMovies.add(Movie("Black Panther", R.drawable.blackp, R.drawable.kgf))
        return lstMovies
    }

    @JvmStatic
    fun getlanguageMovies(): List<Movie> {
        val lstMovies: MutableList<Movie> = ArrayList()
        lstMovies.add(Movie("The Martian", R.drawable.themartian, R.drawable.superman))
        lstMovies.add(Movie("Moana", R.drawable.moana, R.drawable.spidercover))
        lstMovies.add(Movie("Black Panther", R.drawable.blackp, R.drawable.avengers))
        lstMovies.add(Movie("Moana", R.drawable.moana, R.drawable.spidercover))
        lstMovies.add(Movie("The Martian", R.drawable.themartian, R.drawable.batman))
        lstMovies.add(Movie("Black Panther", R.drawable.blackp, R.drawable.kgf))
        return lstMovies
    }
}