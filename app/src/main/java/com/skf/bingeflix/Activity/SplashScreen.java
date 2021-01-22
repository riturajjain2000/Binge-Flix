package com.skf.bingeflix.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.skf.bingeflix.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView tv=findViewById(R.id.tv);
        TextView tv1=findViewById(R.id.textView2);
        int SPLASH = 2500;
        Animation animation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.text_anim);
        tv.startAnimation(animation);
        tv1.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,Login.class));
                finish();
            }
        }, SPLASH);
    }
}