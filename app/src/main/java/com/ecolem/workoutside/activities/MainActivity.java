package com.ecolem.workoutside.activities;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private RelativeLayout mCatalogMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.action_bar_title));
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        mCatalogMenuButton = (RelativeLayout) findViewById(R.id.menu_catalog);
        mCatalogMenuButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_catalog:
                Intent intent = new Intent(MainActivity.this, MovementActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
