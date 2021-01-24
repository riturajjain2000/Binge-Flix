package com.skf.bingeflix.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skf.bingeflix.Adapters.MovieAdapter.MyViewHolder
import com.skf.bingeflix.Models.Movie
import com.skf.bingeflix.R

class MovieAdapter(var context: Context, var mData: List<Movie>, var movieItemClickListener: MovieItemClickListener) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.TvTitle.text = mData[i].title
        myViewHolder.ImgMovie.setImageResource(mData[i].thumbnail)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val TvTitle: TextView
        val ImgMovie: ImageView

        init {
            TvTitle = itemView.findViewById(R.id.item_movie_title)
            ImgMovie = itemView.findViewById(R.id.item_movie_img)
            itemView.setOnClickListener { movieItemClickListener.onMovieClick(mData[adapterPosition], ImgMovie) }
        }
    }
}