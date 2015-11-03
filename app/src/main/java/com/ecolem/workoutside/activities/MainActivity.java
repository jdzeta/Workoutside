package com.ecolem.workoutside.activities;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ecolem.workoutside.R;
import com.firebase.client.Firebase;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private RelativeLayout mCatalogMenuButton;

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialising the object of the FragmentManager. Here I'm passing getSupportFragmentManager(). You can pass getFragmentManager() if you are coding for Android 3.0 or above.
        fragmentManager = getFragmentManager();

        Firebase.setAndroidContext(this);

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
                Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
