package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.manager.CatalogManager;

public class CatalogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
    }
    
    public void initCatalogue(){

        TextView nom = ((TextView) findViewById(R.id.catalogue_nom));
        TextView description = ((TextView) findViewById(R.id.catalogue_description));
        ImageView image = ((ImageView) findViewById(R.id.catalogue_image));

        String cat_name = WorkoutSide.SHARED_PREFS.getString("cat_name", "");

        CatalogManager.getInstance().setCatalogueData(cat_name);
    }

    public void goMouvementsList(View view) {
        
        Intent intent = new Intent(getApplicationContext(), ListMovementActivity.class);
        startActivity(intent);
        finish();
    }
}
