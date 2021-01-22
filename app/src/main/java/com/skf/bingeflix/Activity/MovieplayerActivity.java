package com.skf.bingeflix.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Rational;
import android.view.WindowManager;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource.Factory;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.skf.bingeflix.R;

import java.util.List;

public class MovieplayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;



    @Override
    protected void onUserLeaveHint() {
        PictureInPictureParams pictureInPictureParams = new PictureInPictureParams.Builder().setAspectRatio(new Rational(3, 4)).
        build();
        enterPictureInPictureMode(pictureInPictureParams);
        super.onUserLeaveHint();
    }




    public static final String VIDEO_TEST_URL = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movieplayer);

        iniExoplayer();
    }

    private void iniExoplayer() {
        playerView = findViewById(R.id.movie_exo_player);

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        playerView.setPlayer(simpleExoPlayer);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "appname"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(VIDEO_TEST_URL));
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.release();
    }
}