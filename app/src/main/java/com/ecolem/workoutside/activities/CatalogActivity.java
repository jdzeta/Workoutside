package com.ecolem.workoutside.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ecolem.workoutside.R;
import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.adapter.MovementListAdapter;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Catalog;
import com.ecolem.workoutside.model.Movement;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CatalogActivity extends Activity {

    ListView mListView;
    
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

        setCatalogueData(cat_name);

        mListView = (ListView) findViewById(R.id.catalog_listview);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movement sMovements = (Movement) mListView.getItemAtPosition(position);
                SharedPreferences.Editor editor = WorkoutSide.SHARED_PREFS.edit();
                editor.putString("sMovName", sMovements.getNom());

                editor.commit();

                //Toast.makeText(getApplicationContext(), "View info " + sMouvements.title, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MovementActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void setCatalogueData(String catalogueName){
        Firebase catalogueRef = FirebaseManager.getInstance().getFirebaseRef().child(catalogueName);

        catalogueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " catalog(s)");
                Catalog catalog = snapshot.getValue(Catalog.class);

                ArrayList<Movement> movements = catalog.getMovements();

                Collections.reverse(movements);

                setMouvementListAdapter(movements);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    private void setMouvementListAdapter(ArrayList<Movement> movements) {

        MovementListAdapter adapter = new MovementListAdapter(getApplicationContext(), movements);

        ListView listView = (ListView) findViewById(R.id.catalog_listview);
        listView.setAdapter(adapter);
    }
}
