package com.skf.bingeflix.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skf.bingeflix.R

class MovieDetailActivity : AppCompatActivity() {
    private val MovieThumbnailImg: ImageView? = null
    private val MovieCoverImg: ImageView? = null
    private val tv_title: TextView? = null
    private val tv_description: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        // ini views
        iniViews()
    }

    private fun iniViews() {
        val playFab = findViewById<FloatingActionButton>(R.id.play_fab)
        val movieTitle = intent.extras!!.getString("title")
        val imageResourceId = intent.extras!!.getInt("imgURL")
        val imagecover = intent.extras!!.getInt("imgCover")
        val MovieThumbnailImg = findViewById<ImageView>(R.id.detail_movie_img)
        playFab.setOnClickListener(View.OnClickListener { startActivity(Intent(this@MovieDetailActivity, MovieplayerActivity::class.java)) })
        Glide.with(this).load(imageResourceId).into(MovieThumbnailImg)
        MovieThumbnailImg.setImageResource(imageResourceId)
        val MovieCoverImg = findViewById<ImageView>(R.id.detail_movie_cover)
        Glide.with(this).load(imagecover).into(MovieCoverImg)
        val tv_title = findViewById<TextView>(R.id.detail_movie_title)
        tv_title.setText(movieTitle)
        //getSupportActionBar().setTitle(movieTitle);
        val tv_description = findViewById<TextView>(R.id.detail_movie_desc)
        // setup animation
        MovieCoverImg.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation))
        playFab.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation))
    }
}