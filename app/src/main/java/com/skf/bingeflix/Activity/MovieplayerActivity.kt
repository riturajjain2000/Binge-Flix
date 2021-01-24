package com.skf.bingeflix.Activity

import android.app.PictureInPictureParams
import android.net.Uri
import android.os.Bundle
import android.util.Rational
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.skf.bingeflix.R

class MovieplayerActivity : AppCompatActivity() {

    private var simpleExoPlayer: SimpleExoPlayer? = null
    override fun onUserLeaveHint() {
        val pictureInPictureParams = PictureInPictureParams.Builder().setAspectRatio(Rational(3, 4)).build()
        enterPictureInPictureMode(pictureInPictureParams)
        super.onUserLeaveHint()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_movieplayer)
        iniExoplayer()
    }

    private fun iniExoplayer() {
        val playerView = findViewById<PlayerView>(R.id.movie_exo_player)
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        playerView.player = simpleExoPlayer
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "appname"))
        val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(VIDEO_TEST_URL))
        simpleExoPlayer!!.prepare(videoSource)
        simpleExoPlayer!!.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer!!.release()
    }

    companion object {
        const val VIDEO_TEST_URL = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4"
    }
}