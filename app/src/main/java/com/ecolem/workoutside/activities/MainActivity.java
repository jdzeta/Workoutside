package com.ecolem.workoutside.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.manager.UserManager;
import com.firebase.client.Firebase;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private RelativeLayout mCatalogMenuButton;
    private RelativeLayout mLogoutMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(getResources().getString(R.string.action_bar_title));
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));

        mCatalogMenuButton = (RelativeLayout) findViewById(R.id.menu_catalog);
        mCatalogMenuButton.setOnClickListener(this);
        mLogoutMenuButton = (RelativeLayout) findViewById(R.id.menu_logout);
        mLogoutMenuButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_catalog:
                Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_logout:
                showLogoutAlert();
                break;
            default:
                break;
        }
    }

    private void showLogoutAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle(getResources().getString(R.string.menu_logout));

        // set dialog message
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.logout_alert))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.leave), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        UserManager.getInstance().logout();
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
