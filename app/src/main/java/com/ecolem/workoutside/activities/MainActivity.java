package com.ecolem.workoutside.activities;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.action_bar_title));
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

    }

}
