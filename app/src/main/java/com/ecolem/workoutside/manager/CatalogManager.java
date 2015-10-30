package com.ecolem.workoutside.manager;

import com.ecolem.workoutside.WorkoutSide;
import com.ecolem.workoutside.database.FirebaseManager;
import com.ecolem.workoutside.model.Catalog;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by akawa_000 on 25/10/2015.
 */
public class CatalogManager {

    public static CatalogManager sInstance = null;

    public static CatalogManager getInstance(){
        if(sInstance == null){
            sInstance = new CatalogManager();
        }

        return sInstance;
    }

    public void sendData(Catalog catalog){
        FirebaseManager.getInstance().getFirebaseRef().setValue(catalog.getNom(), catalog);
    }

    public void setCatalogueData(String catalogueName){
        Firebase catalogueRef = FirebaseManager.getInstance().getFirebaseRef().child(catalogueName);

        catalogueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("Found " + snapshot.getChildrenCount() + " catalog(s)");
                Catalog catalog = snapshot.getValue(Catalog.class);

                //@TODO Set catalog activity data
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
