package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecolem.workoutside.R;

/**
 * Created by snabou on 06/11/2015.
 */
public class SplashscreenActivity extends Activity {

    private TextView mSlogan;
    private Animation animFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splashscreen);

        mSlogan = (TextView) findViewById(R.id.slogan);

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.view_fade_in);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SplashscreenActivity.this, StartActivity.class);
                SplashscreenActivity.this.startActivity(mainIntent);
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
        }, 3000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mSlogan.startAnimation(animFadeIn);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}
